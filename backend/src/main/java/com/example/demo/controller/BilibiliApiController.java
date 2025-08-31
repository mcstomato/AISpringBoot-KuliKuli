package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/bilibili")
public class BilibiliApiController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 搜索Bilibili视频
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBilibiliVideos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpServletRequest request) {
        
        logAccessIP(request, "/api/bilibili/search?keyword=" + keyword);
        
        // 重试机制
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                // 添加随机延迟，避免请求过于频繁
                Thread.sleep(200 + (long)(Math.random() * 500));
                
                // 构建Bilibili搜索API URL
                String bilibiliApiUrl = String.format(
                    "https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=%s&page=%d&pagesize=%d",
                    keyword, page, pageSize
                );
                
                // 设置请求头，模拟真实浏览器请求
                HttpHeaders headers = new HttpHeaders();
                headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                headers.set("Referer", "https://search.bilibili.com/");
                headers.set("Accept", "application/json, text/plain, */*");
                headers.set("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
                headers.set("Accept-Encoding", "gzip, deflate, br");
                headers.set("Connection", "keep-alive");
                headers.set("Sec-Fetch-Dest", "empty");
                headers.set("Sec-Fetch-Mode", "cors");
                headers.set("Sec-Fetch-Site", "same-site");
                headers.set("Cache-Control", "no-cache");
                headers.set("Pragma", "no-cache");
                headers.set("Origin", "https://search.bilibili.com");
                headers.set("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"");
                headers.set("sec-ch-ua-mobile", "?0");
                headers.set("sec-ch-ua-platform", "\"Windows\"");
                headers.set("Cookie", "buvid3=random_buvid_" + System.currentTimeMillis() + "_" + retryCount);
                
                HttpEntity<String> entity = new HttpEntity<>(headers);
                
                // 发送请求到Bilibili API
                ResponseEntity<Map> response = restTemplate.exchange(
                    bilibiliApiUrl, 
                    HttpMethod.GET, 
                    entity, 
                    Map.class
                );
            
                Map<String, Object> bilibiliResponse = response.getBody();
                
                if (bilibiliResponse != null && "0".equals(String.valueOf(bilibiliResponse.get("code")))) {
                    // 解析Bilibili返回的数据
                    Map<String, Object> data = (Map<String, Object>) bilibiliResponse.get("data");
                    List<Map<String, Object>> result = (List<Map<String, Object>>) data.get("result");
                    
                    // 转换数据格式
                    List<Map<String, Object>> videos = new ArrayList<>();
                    if (result != null) {
                        for (Map<String, Object> video : result) {
                            Map<String, Object> videoInfo = new HashMap<>();
                            
                            // 安全地获取和清理数据
                            String title = String.valueOf(video.get("title"));
                            String pic = String.valueOf(video.get("pic"));
                            String author = String.valueOf(video.get("author"));
                            
                            // 清理HTML标签
                            title = title.replaceAll("<[^>]*>", "").trim();
                            author = author.replaceAll("<[^>]*>", "").trim();
                            
                            // 确保图片URL是完整的
                            if (pic != null && !pic.equals("null") && !pic.isEmpty()) {
                                if (!pic.startsWith("http")) {
                                    pic = "https:" + pic;
                                }
                            } else {
                                pic = "https://via.placeholder.com/300x200/00A1D6/FFFFFF?text=Video";
                            }
                            
                            videoInfo.put("id", video.get("bvid"));
                            videoInfo.put("title", title);
                            videoInfo.put("thumb", pic);
                            videoInfo.put("duration", formatDuration((String) video.get("duration")));
                            videoInfo.put("viewCount", formatViewCount((Integer) video.get("play")));
                            videoInfo.put("upName", author);
                            // 尝试获取UP头像，如果没有则使用占位符
                            String upAvatar = String.valueOf(video.get("upic"));
                            if (upAvatar != null && !upAvatar.equals("null") && !upAvatar.isEmpty()) {
                                if (!upAvatar.startsWith("http")) {
                                    upAvatar = "https:" + upAvatar;
                                }
                            } else {
                                upAvatar = "https://via.placeholder.com/40x40/FB7299/FFFFFF?text=UP";
                            }
                            videoInfo.put("upAvatar", upAvatar);
                            videoInfo.put("description", video.get("description"));
                            videoInfo.put("publishTime", video.get("pubdate"));
                            videoInfo.put("danmakuCount", formatViewCount((Integer) video.get("video_review")));
                            videoInfo.put("favoriteCount", formatViewCount((Integer) video.get("favorites")));
                            videoInfo.put("coinCount", formatViewCount((Integer) video.get("coins")));
                            videoInfo.put("shareCount", formatViewCount((Integer) video.get("share")));
                            videoInfo.put("likeCount", formatViewCount((Integer) video.get("like")));
                            
                            videos.add(videoInfo);
                        }
                    }
                    
                    Map<String, Object> apiResponse = new HashMap<>();
                    apiResponse.put("success", true);
                    apiResponse.put("videos", videos);
                    apiResponse.put("total", data.get("numResults"));
                    apiResponse.put("page", page);
                    apiResponse.put("pageSize", pageSize);
                    apiResponse.put("keyword", keyword);
                    
                    return ResponseEntity.ok(apiResponse);
                } else {
                    // 检查是否是412错误，如果是则重试
                    if (bilibiliResponse != null && "-412".equals(String.valueOf(bilibiliResponse.get("code")))) {
                        retryCount++;
                        System.out.println("Bilibili API返回412错误，第" + retryCount + "次重试...");
                        if (retryCount < maxRetries) {
                            continue; // 继续重试
                        }
                    }
                    throw new Exception("Bilibili API返回错误: " + bilibiliResponse.get("message"));
                }
            } catch (Exception e) {
                retryCount++;
                System.err.println("Bilibili API调用失败 (第" + retryCount + "次): " + e.getMessage());
                
                // 如果是412错误且还有重试次数，则继续重试
                if (e.getMessage().contains("412") && retryCount < maxRetries) {
                    System.out.println("检测到412错误，等待后重试...");
                    try {
                        Thread.sleep(1000 + (long)(Math.random() * 2000)); // 等待1-3秒
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                
                // 所有重试都失败了
                if (retryCount >= maxRetries) {
                    System.err.println("Bilibili API调用最终失败，已重试" + maxRetries + "次");
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "搜索Bilibili视频失败: " + e.getMessage());
                    response.put("error", "BILIBILI_API_ERROR");
                    return ResponseEntity.badRequest().body(response);
                }
            }
        }
            
        // 如果所有重试都失败了，返回错误
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "搜索Bilibili视频失败: 所有重试都失败了");
        response.put("error", "BILIBILI_API_ERROR");
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 获取视频详情
     */
    @GetMapping("/video/{bvid}")
    public ResponseEntity<Map<String, Object>> getVideoDetail(
            @PathVariable String bvid,
            HttpServletRequest request) {
        
        logAccessIP(request, "/api/bilibili/video/" + bvid);
        
        try {
            // 构建Bilibili视频详情API URL
            String bilibiliApiUrl = "https://api.bilibili.com/x/web-interface/view?bvid=" + bvid;
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            headers.set("Referer", "https://www.bilibili.com");
            headers.set("Accept", "application/json, text/plain, */*");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // 发送请求到Bilibili API
            ResponseEntity<Map> response = restTemplate.exchange(
                bilibiliApiUrl, 
                HttpMethod.GET, 
                entity, 
                Map.class
            );
            
            Map<String, Object> bilibiliResponse = response.getBody();
            
            if (bilibiliResponse != null && "0".equals(String.valueOf(bilibiliResponse.get("code")))) {
                Map<String, Object> data = (Map<String, Object>) bilibiliResponse.get("data");
                
                Map<String, Object> videoInfo = new HashMap<>();
                videoInfo.put("bvid", data.get("bvid"));
                videoInfo.put("aid", data.get("aid"));
                videoInfo.put("title", data.get("title"));
                videoInfo.put("description", data.get("desc"));
                videoInfo.put("pic", data.get("pic"));
                videoInfo.put("duration", formatDuration((Integer) data.get("duration")));
                Map<String, Object> stat = (Map<String, Object>) data.get("stat");
                videoInfo.put("viewCount", formatViewCount((Integer) stat.get("view")));
                videoInfo.put("danmakuCount", formatViewCount((Integer) stat.get("danmaku")));
                videoInfo.put("replyCount", formatViewCount((Integer) stat.get("reply")));
                videoInfo.put("favoriteCount", formatViewCount((Integer) stat.get("favorite")));
                videoInfo.put("coinCount", formatViewCount((Integer) stat.get("coin")));
                videoInfo.put("shareCount", formatViewCount((Integer) stat.get("share")));
                videoInfo.put("likeCount", formatViewCount((Integer) stat.get("like")));
                videoInfo.put("publishTime", data.get("pubdate"));
                
                // UP主信息
                Map<String, Object> owner = (Map<String, Object>) data.get("owner");
                videoInfo.put("upName", owner.get("name"));
                videoInfo.put("upAvatar", owner.get("face"));
                videoInfo.put("upMid", owner.get("mid"));
                
                Map<String, Object> apiResponse = new HashMap<>();
                apiResponse.put("success", true);
                apiResponse.put("video", videoInfo);
                
                return ResponseEntity.ok(apiResponse);
            } else {
                throw new Exception("Bilibili API返回错误: " + bilibiliResponse.get("message"));
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取视频详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/recommend")
    @ResponseBody
    public Map<String, Object> getRecommendVideos(HttpServletRequest request) {
        logAccessIP(request, "/api/bilibili/recommend");
        try {
            String url = "https://api.bilibili.com/x/web-interface/wbi/index/top/feed/rcmd";
            HttpHeaders headers = createBilibiliHeaders();
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, 
                new HttpEntity<>(headers), String.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                
                System.out.println("推荐API原始响应: " + response.getBody().substring(0, Math.min(500, response.getBody().length())));
                
                if (root.get("code").asInt() == 0) {
                    JsonNode data = root.get("data");
                    List<Map<String, Object>> videos = new ArrayList<>();
                    
                    if (data.has("item") && data.get("item").isArray()) {
                        for (JsonNode item : data.get("item")) {
                            Map<String, Object> video = new HashMap<>();
                            video.put("title", cleanHtmlTags(item.get("title").asText()));
                            video.put("author", cleanHtmlTags(item.get("owner").get("name").asText()));
                            video.put("bvid", item.get("bvid").asText());
                            video.put("pic", ensureCompleteUrl(item.get("pic").asText()));
                            video.put("upic", item.get("owner").get("face").asText());
                            video.put("play", formatViewCount(item.get("stat").get("view").asInt()));
                            video.put("video_review", formatViewCount(item.get("stat").get("danmaku").asInt()));
                            video.put("duration", formatDuration(item.get("duration").asInt()));
                            videos.add(video);
                        }
                    }
                    
                    // 不打乱视频列表，保持原始顺序
                    System.out.println("推荐API解析结果: " + videos.size() + " 个视频，保持原始顺序");
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("videos", videos);
                    return result;
                } else {
                    System.out.println("推荐API返回错误码: " + root.get("code").asInt());
                }
            }
        } catch (Exception e) {
            System.err.println("获取推荐视频失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("error", "BILIBILI_API_ERROR");
        return result;
    }

    @GetMapping("/channel/{tid}")
    @ResponseBody
    public Map<String, Object> getChannelVideos(@PathVariable String tid, HttpServletRequest request) {
        logAccessIP(request, "/api/bilibili/channel/" + tid);
        try {
            String url = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=" + tid;
            HttpHeaders headers = createBilibiliHeaders();
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, 
                new HttpEntity<>(headers), String.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                
                System.out.println("频道" + tid + " API原始响应: " + response.getBody().substring(0, Math.min(500, response.getBody().length())));
                
                if (root.get("code").asInt() == 0) {
                    JsonNode data = root.get("data");
                    List<Map<String, Object>> videos = new ArrayList<>();
                    
                    if (data.has("list") && data.get("list").isArray()) {
                        for (JsonNode item : data.get("list")) {
                            Map<String, Object> video = new HashMap<>();
                            video.put("title", cleanHtmlTags(item.get("title").asText()));
                            video.put("author", cleanHtmlTags(item.get("owner").get("name").asText()));
                            video.put("bvid", item.get("bvid").asText());
                            video.put("pic", ensureCompleteUrl(item.get("pic").asText()));
                            video.put("upic", item.get("owner").get("face").asText());
                            video.put("play", formatViewCount(item.get("stat").get("view").asInt()));
                            video.put("video_review", formatViewCount(item.get("stat").get("danmaku").asInt()));
                            video.put("duration", formatDuration(item.get("duration").asInt()));
                            videos.add(video);
                        }
                    }
                    
                    // 不打乱视频列表，保持原始顺序
                    System.out.println("频道" + tid + " API解析结果: " + videos.size() + " 个视频，保持原始顺序");
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("videos", videos);
                    return result;
                } else {
                    System.out.println("频道" + tid + " API返回错误码: " + root.get("code").asInt());
                }
            }
        } catch (Exception e) {
            System.err.println("获取分区视频失败: " + tid + " - " + e.getMessage());
            e.printStackTrace();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("error", "BILIBILI_API_ERROR");
        return result;
    }

    /**
     * 格式化播放时长
     */
    private String formatDuration(String duration) {
        if (duration == null || duration.isEmpty()) {
            return "00:00";
        }
        
        try {
            int seconds = Integer.parseInt(duration);
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            return String.format("%02d:%02d", minutes, remainingSeconds);
        } catch (NumberFormatException e) {
            return duration;
        }
    }

    /**
     * 格式化播放时长（秒数）
     */
    private String formatDuration(Integer duration) {
        if (duration == null) {
            return "00:00";
        }
        
        int minutes = duration / 60;
        int remainingSeconds = duration % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * 格式化播放量
     */
    private String formatViewCount(Integer count) {
        if (count == null) return "0";
        if (count < 10000) {
            return count.toString();
        } else if (count < 100000000) {
            return String.format("%.1f万", count / 10000.0);
        } else {
            return String.format("%.1f亿", count / 100000000.0);
        }
    }

    /**
     * 记录访问IP地址
     */
    private void logAccessIP(HttpServletRequest request, String endpoint) {
        String clientIP = getClientIP(request);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("🌐 Bilibili API访问记录 | IP: " + clientIP + " | 路径: " + endpoint + " | 时间: " + timestamp);
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIP(HttpServletRequest request) {
        // 优先获取X-Forwarded-For头（代理服务器转发）
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        // 获取X-Real-IP头（Nginx等代理服务器设置）
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        // 获取X-Forwarded-For-Original头
        String xForwardedForOriginal = request.getHeader("X-Forwarded-For-Original");
        if (xForwardedForOriginal != null && !xForwardedForOriginal.isEmpty()) {
            return xForwardedForOriginal;
        }
        
        // 最后获取直接连接的IP地址
        return request.getRemoteAddr();
    }

    /**
     * 创建Bilibili API请求头
     */
    private HttpHeaders createBilibiliHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        headers.set("Referer", "https://www.bilibili.com");
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.set("Accept-Encoding", "gzip, deflate, br");
        headers.set("Connection", "keep-alive");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-site");
        headers.set("Cache-Control", "no-cache");
        headers.set("Pragma", "no-cache");
        headers.set("Origin", "https://www.bilibili.com");
        headers.set("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"Windows\"");
        headers.set("Cookie", "buvid3=random_buvid_" + System.currentTimeMillis());
        return headers;
    }

    /**
     * 清理HTML标签
     */
    private String cleanHtmlTags(String text) {
        if (text == null) return "";
        return text.replaceAll("<[^>]*>", "");
    }

    /**
     * 确保URL完整
     */
    private String ensureCompleteUrl(String url) {
        if (url == null) return "";
        if (url.startsWith("//")) {
            return "https:" + url;
        }
        return url;
    }
}
