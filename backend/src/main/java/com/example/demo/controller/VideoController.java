package com.example.demo.controller;

import com.example.demo.dao.BilibiliVideoRepository;
import com.example.demo.model.BilibiliVideo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String videoPage() {
        return "video.html";
    }

    @GetMapping("/play")
    public String playPage() {
        return "forward:/play.html";
    }

    @GetMapping("/play/{id}")
    public String playVideo(@PathVariable String id) {
        return "forward:/play.html";
    }

    @GetMapping("/api/videos/random")
    @ResponseBody
    public List<Map<String, Object>> randomVideos() {
        try {
            List<BilibiliVideo> list = repository.findRandomEight();
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
    public Map<String, Object> getVideoById(@PathVariable String id) {
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
}


