import requests
import random
import json
import time
import os
import mysql.connector
from mysql.connector import Error
from datetime import datetime

# 数据库配置
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': '123456',
    'database': 'bilibili_data',
    'charset': 'utf8mb4',
    'collation': 'utf8mb4_unicode_ci',
    'use_unicode': True
}

# 代理配置（如果需要）
PROXIES = None
# PROXIES = {
#     "http": "http://127.0.0.1:10809",
#     "https": "http://127.0.0.1:10809",
# }

def get_bilibili_ranking_videos(rid=0, type='all'):
    """
    获取Bilibili排行榜视频列表
    """
    url = "https://api.bilibili.com/x/web-interface/ranking/v2"
    
    # 更真实的浏览器头部信息
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Referer": "https://www.bilibili.com/",
        "Origin": "https://www.bilibili.com",
        "Accept": "application/json, text/plain, */*",
        "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8",
        "Accept-Encoding": "gzip, deflate, br",
        "Connection": "keep-alive",
        "Sec-Fetch-Dest": "empty",
        "Sec-Fetch-Mode": "cors",
        "Sec-Fetch-Site": "same-site",
        "Pragma": "no-cache",
        "Cache-Control": "no-cache",
        "sec-ch-ua": '"Not_A Brand";v="8", "Chromium";v="120", "Google Chrome";v="120"',
        "sec-ch-ua-mobile": "?0",
        "sec-ch-ua-platform": '"Windows"'
    }
    
    params = {
        'rid': rid,
        'type': type
    }

    try:
        print(f"正在请求排行榜数据...")
        response = requests.get(
            url, 
            headers=headers, 
            params=params, 
            timeout=15,
            proxies=PROXIES,
            verify=False  # 忽略SSL验证
        )
        
        print(f"响应状态码: {response.status_code}")
        print(f"响应内容类型: {response.headers.get('content-type')}")
        
        # 检查响应内容
        if not response.text.strip():
            print("❌ 服务器返回空响应")
            return None
            
        # 尝试解析JSON
        try:
            data = response.json()
        except json.JSONDecodeError as e:
            print(f"❌ JSON解析失败: {e}")
            print(f"响应内容前200字符: {response.text[:200]}")
            return None

        if data.get('code') == 0:
            print(f"✅ 成功获取 {len(data['data']['list'])} 个视频")
            return data['data']['list']
        else:
            print(f"❌ 获取排行榜视频失败: {data.get('message', '未知错误')} (代码: {data.get('code', '未知')})")
            return None
            
    except requests.exceptions.RequestException as e:
        print(f"❌ 网络请求错误: {e}")
        return None
    except Exception as e:
        print(f"❌ 未知错误: {e}")
        return None

def get_video_detail(aid, bvid):
    """
    获取视频详细信息
    """
    url = "https://api.bilibili.com/x/web-interface/view"
    
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Referer": f"https://www.bilibili.com/video/{bvid}",
        "Origin": "https://www.bilibili.com",
        "Accept": "application/json, text/plain, */*"
    }

    params = {'bvid': bvid}

    try:
        response = requests.get(
            url, 
            headers=headers, 
            params=params, 
            timeout=15,
            proxies=PROXIES,
            verify=False
        )
        
        if not response.text.strip():
            print("❌ 视频详情返回空响应")
            return None
            
        try:
            data = response.json()
        except json.JSONDecodeError:
            print("❌ 视频详情JSON解析失败")
            return None

        if data.get('code') == 0:
            return data['data']
        else:
            print(f"❌ 获取视频详情失败: {data.get('message')}")
            return None
            
    except Exception as e:
        print(f"❌ 获取视频详情出错: {e}")
        return None

def get_popular_as_fallback():
    """
    备用方案：使用热门视频接口
    """
    print("尝试使用热门视频接口作为备选...")
    url = "https://api.bilibili.com/x/web-interface/popular"
    
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Referer": "https://www.bilibili.com/"
    }

    try:
        response = requests.get(url, headers=headers, timeout=15, proxies=PROXIES, verify=False)
        data = response.json()
        
        if data.get('code') == 0:
            print(f"✅ 成功获取 {len(data['data']['list'])} 个热门视频")
            return data['data']['list']
        else:
            print(f"❌ 获取热门视频失败")
            return None
            
    except Exception as e:
        print(f"❌ 获取热门视频出错: {e}")
        return None

def create_database_connection():
    """
    创建数据库连接
    """
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        if connection.is_connected():
            print("✅ 成功连接到MySQL数据库")
            cursor = connection.cursor()
            cursor.execute("SET NAMES utf8mb4")
            cursor.execute("SET CHARACTER SET utf8mb4")
            cursor.close()
            return connection
    except Error as e:
        print(f"❌ 连接数据库时出错: {e}")
        return None

def save_to_database(video_data):
    """
    将视频数据保存到MySQL数据库
    """
    connection = create_database_connection()
    if connection is None:
        return False

    cursor = connection.cursor()

    try:
        insert_query = """
        INSERT INTO bilibili_videos
        (title, up_name, up_face_url, view_count, danmaku_count, duration_time, aid, bvid, cid,
         publish_time, cover_url, video_url, embed_url, fetch_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
        view_count = VALUES(view_count),
        danmaku_count = VALUES(danmaku_count),
        fetch_time = VALUES(fetch_time)
        """

        cursor.execute(insert_query, (
            video_data['title'],
            video_data['up_name'],
            video_data['up_face_url'],
            video_data['view_count'],
            video_data['danmaku_count'],
            video_data['duration_time'],
            video_data['aid'],
            video_data['bvid'],
            video_data['cid'],
            video_data['publish_time'],
            video_data['cover_url'],
            video_data['video_url'],
            video_data['embed_url'],
            video_data['fetch_time']
        ))

        connection.commit()
        print(f"✅ 视频数据已保存: {video_data['title'][:20]}...")
        return True

    except Error as e:
        print(f"❌ 保存到数据库时出错: {e}")
        return False
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()

def main():
    """
    主函数
    """
    print("=" * 60)
    print("B站视频数据采集程序")
    print("=" * 60)

    # 首先尝试排行榜接口
    print("\n正在获取Bilibili排行榜视频信息...")
    ranking_videos = get_bilibili_ranking_videos()

    # 如果排行榜接口失败，尝试热门视频接口
    if not ranking_videos:
        print("\n排行榜接口失败，尝试热门视频接口...")
        ranking_videos = get_popular_as_fallback()

    if not ranking_videos:
        print("❌ 所有接口都失败了，程序退出")
        return

    # 获取视频信息
    success_count = 0
    target_count = min(100, len(ranking_videos))  # 减少数量以避免频繁请求

    for i in range(target_count):
        print(f"\n正在处理第 {i + 1}/{target_count} 个视频...")
        print("-" * 40)

        video = ranking_videos[i]
        video_detail = get_video_detail(video.get('aid'), video.get('bvid'))

        if video_detail:
            # 构建视频信息
            video_info = {
                "title": video.get('title', ''),
                "up_name": video.get('owner', {}).get('name', ''),
                "view_count": video.get('stat', {}).get('view', 0),
                "danmaku_count": video.get('stat', {}).get('danmaku', 0),
                "duration_time": time.strftime("%H:%M:%S", time.gmtime(video.get('duration', 0))),
                "aid": video.get('aid'),
                "bvid": video.get('bvid'),
                "cid": video_detail.get('cid'),
                "publish_time": datetime.fromtimestamp(video.get('pubdate', 0)).strftime("%Y-%m-%d %H:%M:%S"),
                "cover_url": video_detail.get('pic'),
                "video_url": f"https://www.bilibili.com/video/{video.get('bvid')}",
                "embed_url": f"https://player.bilibili.com/player.html?aid={video.get('aid')}&bvid={video.get('bvid')}&cid={video_detail.get('cid')}",
                "fetch_time": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                "up_face_url": video.get('owner', {}).get('face', '')
            }

            # 保存到数据库
            if save_to_database(video_info):
                success_count += 1
                print(f"   UP主: {video_info['up_name']} | 播放: {video_info['view_count']}")
            else:
                print(f"❌ 保存失败")
        else:
            print(f"❌ 获取视频详情失败")

        # 增加延迟避免被封
        time.sleep(0.1)

    print(f"\n✅ 完成！成功处理 {success_count} 个视频")

if __name__ == "__main__":
    # 忽略SSL警告
    import urllib3
    urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
    
    main()