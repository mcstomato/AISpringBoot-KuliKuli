package com.example.demo.shell;

import com.example.demo.dao.BilibiliVideoRepository;
import com.example.demo.model.BilibiliVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 视频管理命令类
 * 提供视频相关的Shell命令操作
 */
@ShellComponent
public class VideoCommands {

    @Autowired
    private BilibiliVideoRepository videoRepository;

    /**
     * 查看所有视频
     */
    @ShellMethod(value = "查看所有视频列表", key = "videos list")
    public String listVideos() {
        List<BilibiliVideo> videos = videoRepository.findAll();
        
        if (videos.isEmpty()) {
            return "暂无视频数据";
        }

        StringBuilder result = new StringBuilder();
        result.append("视频列表：\n");
        result.append("┌─────────┬─────────────────────────────────────────────────────────────────────────────────┬─────────────────┬──────────────┐\n");
        result.append("│   ID    │                             标题                                                │      作者       │   播放量     │\n");
        result.append("├─────────┼─────────────────────────────────────────────────────────────────────────────────┼─────────────────┼──────────────┤\n");

        for (BilibiliVideo video : videos) {
            String title = video.getTitle();
            if (title.length() > 70) {
                title = title.substring(0, 67) + "...";
            }
            String author = video.getUpName() != null ? video.getUpName() : "未知";
            String viewCount = video.getViewCount() != null ? video.getViewCount().toString() : "0";
            
            result.append(String.format("│ %7d │ %-70s │ %-15s │ %-12s │\n", 
                    video.getId(), 
                    title, 
                    author,
                    viewCount));
        }
        result.append("└─────────┴─────────────────────────────────────────────────────────────────────────────────┴─────────────────┴──────────────┘\n");
        result.append("总计: ").append(videos.size()).append(" 个视频");
        return result.toString();
    }

    /**
     * 查看指定视频详情
     */
    @ShellMethod(value = "查看指定视频详情", key = "videos show")
    public String showVideo(
            @ShellOption(value = "id", help = "视频ID", defaultValue = ShellOption.NULL) Integer id
    ) {
        Optional<BilibiliVideo> videoOpt = videoRepository.findById(id);
        if (!videoOpt.isPresent()) {
            return "视频不存在";
        }

        BilibiliVideo video = videoOpt.get();

        return String.format("视频详情：\n" +
                "==================================================\n" +
                " ID:           %d\n" +
                " 标题:         %s\n" +
                " 作者:         %s\n" +
                " 视频链接:     %s\n" +
                " 封面链接:     %s\n" +
                " 播放量:       %d\n" +
                " 弹幕数:       %d\n" +
                "==================================================",
                video.getId(),
                video.getTitle(),
                video.getUpName() != null ? video.getUpName() : "未知",
                video.getVideoUrl() != null ? video.getVideoUrl() : "未知",
                video.getCoverUrl() != null ? video.getCoverUrl() : "未知",
                video.getViewCount() != null ? video.getViewCount() : 0,
                video.getDanmakuCount() != null ? video.getDanmakuCount() : 0
        );
    }

    /**
     * 搜索视频
     */
    @ShellMethod(value = "搜索视频", key = "videos search")
    public String searchVideos(
            @ShellOption(value = "keyword", help = "搜索关键词") String keyword
    ) {
        List<BilibiliVideo> allVideos = videoRepository.findAll();
        List<BilibiliVideo> filteredVideos = allVideos.stream()
                .filter(video -> 
                    (video.getTitle() != null && video.getTitle().toLowerCase().contains(keyword.toLowerCase())) ||
                    (video.getUpName() != null && video.getUpName().toLowerCase().contains(keyword.toLowerCase()))
                )
                .collect(Collectors.toList());

        if (filteredVideos.isEmpty()) {
            return "未找到匹配的视频";
        }

        StringBuilder result = new StringBuilder();
        result.append("搜索结果：\n");
        result.append("┌─────────┬─────────────────────────────────────────────────────────────────────────────────┐\n");
        result.append("│   ID    │                             标题                                                │\n");
        result.append("├─────────┼─────────────────────────────────────────────────────────────────────────────────┤\n");

        for (BilibiliVideo video : filteredVideos) {
            String title = video.getTitle();
            if (title.length() > 70) {
                title = title.substring(0, 67) + "...";
            }
            result.append(String.format("│ %7d │ %-70s │\n", video.getId(), title));
        }
        result.append("└─────────┴─────────────────────────────────────────────────────────────────────────────────┘\n");
        result.append("找到: ").append(filteredVideos.size()).append(" 个视频");
        return result.toString();
    }

    /**
     * 添加新视频
     */
    @ShellMethod(value = "添加新视频", key = "videos add")
    public String addVideo(
            @ShellOption(value = "title", help = "视频标题") String title,
            @ShellOption(value = "author", help = "作者") String author,
            @ShellOption(value = "videoUrl", help = "视频链接") String videoUrl,
            @ShellOption(value = "coverUrl", help = "封面链接") String coverUrl,
            @ShellOption(value = "playCount", help = "播放量") Integer playCount,
            @ShellOption(value = "likeCount", help = "点赞数") Integer likeCount
    ) {
        BilibiliVideo video = new BilibiliVideo();
        video.setTitle(title);
        video.setUpName(author);
        video.setVideoUrl(videoUrl);
        video.setCoverUrl(coverUrl);
        video.setViewCount(playCount != null ? playCount : 0);
        video.setDanmakuCount(0);

        BilibiliVideo savedVideo = videoRepository.save(video);
        return "视频添加成功！ID: " + savedVideo.getId();
    }

    /**
     * 删除视频
     */
    @ShellMethod(value = "删除视频", key = "videos delete")
    public String deleteVideo(
            @ShellOption(value = "id", help = "视频ID", defaultValue = ShellOption.NULL) Integer id
    ) {
        Optional<BilibiliVideo> videoOpt = videoRepository.findById(id);
        if (!videoOpt.isPresent()) {
            return "视频不存在";
        }

        videoRepository.deleteById(id);
        return "视频删除成功！";
    }

    /**
     * 查看热门视频（按播放量排序）
     */
    @ShellMethod(value = "查看热门视频（按播放量排序）", key = "videos hot")
    public String showHotVideos(
            @ShellOption(value = "limit", help = "显示数量", defaultValue = "10") Integer limit
    ) {
        List<BilibiliVideo> videos = videoRepository.findAll();
        List<BilibiliVideo> hotVideos = videos.stream()
                .sorted((v1, v2) -> {
                    Integer play1 = v1.getViewCount() != null ? v1.getViewCount() : 0;
                    Integer play2 = v2.getViewCount() != null ? v2.getViewCount() : 0;
                    return play2.compareTo(play1); // 降序排列
                })
                .limit(limit)
                .collect(Collectors.toList());

        if (hotVideos.isEmpty()) {
            return "暂无视频数据";
        }

        StringBuilder result = new StringBuilder();
        result.append("热门视频（按播放量排序）：\n");
        result.append("┌─────────┬─────────────────────────────────────────────────────────────────────────────────┬──────────────┐\n");
        result.append("│   ID    │                             标题                                                │   播放量     │\n");
        result.append("├─────────┼─────────────────────────────────────────────────────────────────────────────────┼──────────────┤\n");

        for (BilibiliVideo video : hotVideos) {
            String title = video.getTitle();
            if (title.length() > 70) {
                title = title.substring(0, 67) + "...";
            }
            String viewCount = video.getViewCount() != null ? video.getViewCount().toString() : "0";
            
            result.append(String.format("│ %7d │ %-70s │ %-12s │\n", 
                    video.getId(), 
                    title, 
                    viewCount));
        }
        result.append("└─────────┴─────────────────────────────────────────────────────────────────────────────────┴──────────────┘\n");
        result.append("显示: ").append(hotVideos.size()).append(" 个视频");
        return result.toString();
    }

    /**
     * 查看视频统计信息
     */
    @ShellMethod(value = "查看视频统计信息", key = "videos stats")
    public String showVideoStats() {
        List<BilibiliVideo> videos = videoRepository.findAll();
        
        if (videos.isEmpty()) {
            return "暂无视频数据";
        }

        long totalViews = videos.stream()
                .mapToLong(video -> video.getViewCount() != null ? video.getViewCount() : 0)
                .sum();
        
        long totalDanmaku = videos.stream()
                .mapToLong(video -> video.getDanmakuCount() != null ? video.getDanmakuCount() : 0)
                .sum();

        double avgViews = videos.size() > 0 ? (double) totalViews / videos.size() : 0;
        double avgDanmaku = videos.size() > 0 ? (double) totalDanmaku / videos.size() : 0;

        return String.format("视频统计信息：\n" +
                "==================================================\n" +
                " 总视频数:     %d\n" +
                " 总播放量:     %d\n" +
                " 总弹幕数:     %d\n" +
                " 平均播放量:   %.1f\n" +
                " 平均弹幕数:   %.1f\n" +
                "==================================================",
                videos.size(),
                totalViews,
                totalDanmaku,
                avgViews,
                avgDanmaku
        );
    }
}
