package com.example.demo.controller;

import com.example.demo.dao.BilibiliVideoRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.model.BilibiliVideo;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, Object>> searchVideos(@RequestParam String keyword) {
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
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String keyword) {
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
    public ResponseEntity<Map<String, Object>> searchAll(@RequestParam String keyword) {
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
}
