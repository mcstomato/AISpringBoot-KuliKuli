package com.example.demo.controller;

import com.example.demo.dao.BilibiliVideoRepository;
import com.example.demo.model.BilibiliVideo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class VideoController {

    private final BilibiliVideoRepository repository;

    public VideoController(BilibiliVideoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/video")
    public String videoPage(HttpServletRequest request) {
        logAccessIP(request, "/video");
        return "video.html";
    }

    @GetMapping("/play")
    public String playPage(HttpServletRequest request) {
        logAccessIP(request, "/play");
        return "forward:/play.html";
    }

    @GetMapping("/play/{id}")
    public String playVideo(@PathVariable String id, HttpServletRequest request) {
        logAccessIP(request, "/play/" + id);
        return "forward:/play.html";
    }

    @GetMapping("/api/videos/random")
    @ResponseBody
    public List<Map<String, Object>> randomVideos(HttpServletRequest request) {
        logAccessIP(request, "/api/videos/random");
        try {
            List<BilibiliVideo> list = repository.findRandomThirtyTwo();
            return list.stream().map(v -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", v.getId());
                m.put("title", v.getTitle());
                m.put("duration", v.getDurationTime());
                m.put("views", v.getViewCount());
                m.put("danmaku", v.getDanmakuCount());
                m.put("coverUrl", v.getCoverUrl());
                m.put("upFaceUrl", v.getUpFaceUrl());
                m.put("upName", v.getUpName());
                m.put("up_name", v.getUpName());
                m.put("videoUrl", v.getVideoUrl());
                m.put("embedUrl", v.getEmbedUrl());
                return m;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/api/videos/{id}")
    @ResponseBody
    public Map<String, Object> getVideoById(@PathVariable String id, HttpServletRequest request) {
        logAccessIP(request, "/api/videos/" + id);
        try {
            Integer videoId = Integer.parseInt(id);
            Optional<BilibiliVideo> video = repository.findById(videoId);
            if (video.isPresent()) {
                BilibiliVideo v = video.get();
                Map<String, Object> m = new HashMap<>();
                m.put("id", v.getId());
                m.put("title", v.getTitle());
                m.put("duration", v.getDurationTime());
                m.put("views", v.getViewCount());
                m.put("danmaku", v.getDanmakuCount());
                m.put("coverUrl", v.getCoverUrl());
                m.put("upFaceUrl", v.getUpFaceUrl());
                m.put("upName", v.getUpName());
                m.put("videoUrl", v.getVideoUrl());
                m.put("embedUrl", v.getEmbedUrl());
                m.put("publishTime", v.getPublishTime());
                return m;
            }
            return new HashMap<>();
        } catch (NumberFormatException e) {
            System.err.println("Invalid video ID format: " + id);
            return new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error fetching video with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    
    /**
     * 记录访问IP地址
     */
    private void logAccessIP(HttpServletRequest request, String path) {
        String clientIP = getClientIP(request);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("🌐 访问记录 | IP: " + clientIP + " | 路径: " + path + " | 时间: " + timestamp);
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


