package com.example.demo.controller;

import com.example.demo.dao.BilibiliVideoRepository;
import com.example.demo.model.BilibiliVideo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class VideoController {

    private final BilibiliVideoRepository repository;

    public VideoController(BilibiliVideoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/videos/random")
    public List<Map<String, Object>> randomVideos() {
        List<BilibiliVideo> list = repository.findRandomEight();
        return list.stream().map(v -> {
            Map<String, Object> m = new HashMap<>();
            m.put("title", v.getTitle());
            m.put("duration", v.getDurationTime());
            m.put("views", v.getViewCount());
            m.put("danmaku", v.getDanmakuCount());
            m.put("coverUrl", v.getCoverUrl());
            m.put("upFaceUrl", v.getUpFaceUrl());
            m.put("upName", v.getUpName());
            m.put("up_name", v.getUpName());
            return m;
        }).collect(Collectors.toList());
    }
}


