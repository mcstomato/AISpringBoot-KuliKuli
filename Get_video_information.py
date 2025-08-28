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
    'user': 'root',  # 替换为你的MySQL用户名
    'password': '123456',  # 替换为你的MySQL密码
    'database': 'bilibili_data',
    'charset': 'utf8mb4'
}


def get_bilibili_hot_videos():
    """
    获取Bilibili热门视频列表
    """
    url = "https://api.bilibili.com/x/web-interface/popular"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "Referer": "https://www.bilibili.com/"
    }

    try:
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()
        data = response.json()

        if data['code'] == 0:
            return data['data']['list']
        else:
            print(f"获取热门视频失败: {data['message']}")
            return None
    except Exception as e:
        print(f"请求出错: {e}")
        return None


def get_video_detail(aid, bvid):
    """
    获取视频详细信息，包括CID
    """
    url = "https://api.bilibili.com/x/web-interface/view"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "Referer": "https://www.bilibili.com/"
    }

    params = {}
    if aid:
        params['aid'] = aid
    if bvid:
        params['bvid'] = bvid

    try:
        response = requests.get(url, headers=headers, params=params, timeout=10)
        response.raise_for_status()
        data = response.json()

        if data['code'] == 0:
            return data['data']
        else:
            print(f"获取视频详情失败: {data['message']}")
            return None
    except Exception as e:
        print(f"请求出错: {e}")
        return None


def get_random_hot_video_info():
    """
    随机获取一个热门视频的信息
    """
    # 获取热门视频列表
    hot_videos = get_bilibili_hot_videos()

    if not hot_videos:
        print("无法获取热门视频列表")
        return None

    # 随机选择一个视频
    random_video = random.choice(hot_videos)

    # 获取视频详细信息
    video_detail = get_video_detail(random_video['aid'], random_video['bvid'])

    if not video_detail:
        print("无法获取视频详细信息")
        return None

    # 提取所需信息（新增up_face_url字段）
    video_info = {
        "title": random_video['title'],
        "up_name": random_video['owner']['name'],
        "view_count": random_video['stat']['view'],
        "danmaku_count": random_video['stat']['danmaku'],
        "duration_time": time.strftime("%H:%M:%S", time.gmtime(random_video['duration'])),
        "aid": random_video['aid'],
        "bvid": random_video['bvid'],
        "cid": video_detail['cid'],
        "publish_time": datetime.fromtimestamp(random_video['pubdate']).strftime("%Y-%m-%d %H:%M:%S"),
        "cover_url": video_detail.get('pic'),
        "video_url": f"https://www.bilibili.com/video/{random_video['bvid']}",
        "embed_url": f"https://player.bilibili.com/player.html?isOutside=true&amp;aid={random_video['aid']}&bvid={random_video['bvid']}&cid={video_detail['cid']}&amp;page=1",
        "fetch_time": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
        # 新增Up主头像URL字段
        "up_face_url": random_video['owner']['face']  # 从API响应中获取头像URL
    }

    return video_info


def create_database_connection():
    """
    创建数据库连接
    """
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        if connection.is_connected():
            print("成功连接到MySQL数据库")
            return connection
    except Error as e:
        print(f"连接数据库时出错: {e}")
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
                       VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) ON DUPLICATE KEY \
                       UPDATE \
                           view_count = \
                       VALUES (view_count), danmaku_count = \
                       VALUES (danmaku_count), fetch_time = \
                       VALUES (fetch_time), up_face_url = \
                       VALUES (up_face_url) \
                       """

        cursor.execute(insert_query, (
            video_data['title'],
            video_data['up_name'],
            video_data['up_face_url'],  # 新增参数
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
        print(f"✅ 视频数据已保存到数据库: {video_data['title'][:30]}...")
        return True

    except Error as e:
        print(f"❌ 保存到数据库时出错: {e}")
        return False

    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def print_database_summary():
    """
    打印数据库中的视频汇总信息
    """
    connection = create_database_connection()
    if connection is None:
        return

    cursor = connection.cursor(dictionary=True)

    try:
        # 获取总记录数
        cursor.execute("SELECT COUNT(*) as total FROM bilibili_videos")
        total_count = cursor.fetchone()['total']

        # 获取最新添加的视频
        cursor.execute("""
                       SELECT title, up_name, view_count
                       FROM bilibili_videos
                       ORDER BY id DESC LIMIT 10
                       """)
        recent_videos = cursor.fetchall()

        print("\n" + "=" * 60)
        print("数据库汇总信息:")
        print("=" * 60)
        print(f"总视频数量: {total_count}")
        print("\n最近添加的视频:")
        print("-" * 60)

        for i, video in enumerate(recent_videos, 1):
            print(f"{i:2d}. {video['title'][:30]}... - {video['up_name']} (播放: {video['view_count']})")

    except Error as e:
        print(f"读取数据库时出错: {e}")

    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()


def main():
    """
    主函数
    """
    print("正在获取Bilibili热门视频信息...")

    # 获取10次视频信息
    success_count = 0
    for i in range(100):
        print(f"\n正在获取第 {i + 1} 个视频信息...")
        print("-" * 40)

        video_info = get_random_hot_video_info()

        if video_info:
            # 保存到数据库
            if save_to_database(video_info):
                success_count += 1
                print(f"   UP主: {video_info['up_name']} | 播放数: {video_info['view_count']}")
            else:
                print(f"❌ 保存第 {i + 1} 个视频信息到数据库失败")
        else:
            print(f"❌ 获取第 {i + 1} 个视频信息失败")

        # 添加短暂延迟，避免请求过于频繁
        time.sleep(1)

    # 打印数据库汇总信息
    print(f"\n成功获取并保存了 {success_count} 个视频信息")
    print_database_summary()


if __name__ == "__main__":
    main()