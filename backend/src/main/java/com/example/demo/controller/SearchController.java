package com.example.demo.controller;

import com.example.demo.dao.BilibiliVideoRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.model.BilibiliVideo;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private BilibiliVideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/videos")
    public ResponseEntity<Map<String, Object>> searchVideos(@RequestParam String keyword, HttpServletRequest request) {
        logAccessIP(request, "/api/search/videos?keyword=" + keyword);
        try {
            List<BilibiliVideo> videos = videoRepository.findByTitleContainingIgnoreCaseOrUpNameContainingIgnoreCase(keyword, keyword);
            
                            List<Map<String, Object>> videoResults = videos.stream().map(video -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", video.getId());
                    result.put("title", video.getTitle());
                    result.put("thumb", video.getCoverUrl() != null ? video.getCoverUrl() : "https://via.placeholder.com/300x200/00A1D6/FFFFFF?text=Video");
                    result.put("duration", video.getDurationTime() != null ? video.getDurationTime() : "00:00");
                    result.put("viewCount", formatViewCount(video.getViewCount()));
                    result.put("upName", video.getUpName());
                    result.put("upAvatar", video.getUpFaceUrl() != null ? video.getUpFaceUrl() : "https://via.placeholder.com/40x40/FB7299/FFFFFF?text=UP");
                    return result;
                }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", videoResults);
            response.put("total", videoResults.size());
            response.put("keyword", keyword);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索视频失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String keyword, HttpServletRequest request) {
        logAccessIP(request, "/api/search/users?keyword=" + keyword);
        try {
            List<User> users = userRepository.findByNicknameContainingIgnoreCaseOrLoginAccountContainingIgnoreCase(keyword, keyword);
            
            List<Map<String, Object>> userResults = users.stream().map(user -> {
                Map<String, Object> result = new HashMap<>();
                result.put("id", user.getId());
                result.put("name", user.getNickname() != null ? user.getNickname() : user.getLoginAccount());
                result.put("avatar", user.getAvatarUrl() != null ? user.getAvatarUrl() : "https://via.placeholder.com/80x80/FB7299/FFFFFF?text=User");
                result.put("videoCount", user.getVideoCount() != null ? user.getVideoCount().toString() : "0");
                result.put("followerCount", formatFollowerCount(user.getFollowerCount()));
                result.put("loginAccount", user.getLoginAccount());
                return result;
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userResults);
            response.put("total", userResults.size());
            response.put("keyword", keyword);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索用户失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> searchAll(@RequestParam String keyword, HttpServletRequest request) {
        logAccessIP(request, "/api/search/all?keyword=" + keyword);
        try {
            // 搜索视频
            List<BilibiliVideo> videos = videoRepository.findByTitleContainingIgnoreCaseOrUpNameContainingIgnoreCase(keyword, keyword);
            List<Map<String, Object>> videoResults = videos.stream().map(video -> {
                Map<String, Object> result = new HashMap<>();
                result.put("id", video.getId());
                result.put("title", video.getTitle());
                                 result.put("thumb", video.getCoverUrl() != null ? video.getCoverUrl() : "https://via.placeholder.com/300x200/00A1D6/FFFFFF?text=Video");
                 result.put("duration", video.getDurationTime() != null ? video.getDurationTime() : "00:00");
                 result.put("viewCount", formatViewCount(video.getViewCount()));
                 result.put("upName", video.getUpName());
                 result.put("upAvatar", video.getUpFaceUrl() != null ? video.getUpFaceUrl() : "https://via.placeholder.com/40x40/FB7299/FFFFFF?text=Video");
                 result.put("type", "video");
                return result;
            }).collect(Collectors.toList());

            // 搜索用户
            List<User> users = userRepository.findByNicknameContainingIgnoreCaseOrLoginAccountContainingIgnoreCase(keyword, keyword);
            List<Map<String, Object>> userResults = users.stream().map(user -> {
                Map<String, Object> result = new HashMap<>();
                result.put("id", user.getId());
                result.put("name", user.getNickname() != null ? user.getNickname() : user.getLoginAccount());
                result.put("avatar", user.getAvatarUrl() != null ? user.getAvatarUrl() : "https://via.placeholder.com/80x80/FB7299/FFFFFF?text=User");
                result.put("videoCount", user.getVideoCount() != null ? user.getVideoCount().toString() : "0");
                result.put("followerCount", formatFollowerCount(user.getFollowerCount()));
                result.put("loginAccount", user.getLoginAccount());
                result.put("type", "user");
                return result;
            }).collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("videos", videoResults);
            response.put("users", userResults);
            response.put("totalVideos", videoResults.size());
            response.put("totalUsers", userResults.size());
            response.put("keyword", keyword);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private String formatViewCount(Integer viewCount) {
        if (viewCount == null) return "0";
        if (viewCount < 10000) {
            return viewCount.toString();
        } else if (viewCount < 100000000) {
            return String.format("%.1f万", viewCount / 10000.0);
        } else {
            return String.format("%.1f亿", viewCount / 100000000.0);
        }
    }

    private String formatFollowerCount(Integer followerCount) {
        if (followerCount == null) return "0";
        if (followerCount < 10000) {
            return followerCount.toString();
        } else {
            return String.format("%.1f万", followerCount / 10000.0);
        }
    }

    /**
     * 记录访问IP地址
     */
    private void logAccessIP(HttpServletRequest request, String endpoint) {
        String clientIP = getClientIP(request);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("🌐 访问记录 | IP: " + clientIP + " | 路径: " + endpoint + " | 时间: " + timestamp);
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
}
