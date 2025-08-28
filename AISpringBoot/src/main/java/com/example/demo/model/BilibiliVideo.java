package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bilibili_videos")
public class BilibiliVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "up_name")
    private String upName;

    @Column(name = "up_face_url")
    private String upFaceUrl;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "danmaku_count")
    private Integer danmakuCount;

    @Column(name = "duration_time")
    private String durationTime;

    @Column(name = "cover_url")
    private String coverUrl;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUpName() { return upName; }
    public void setUpName(String upName) { this.upName = upName; }
    public String getUpFaceUrl() { return upFaceUrl; }
    public void setUpFaceUrl(String upFaceUrl) { this.upFaceUrl = upFaceUrl; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Integer getDanmakuCount() { return danmakuCount; }
    public void setDanmakuCount(Integer danmakuCount) { this.danmakuCount = danmakuCount; }
    public String getDurationTime() { return durationTime; }
    public void setDurationTime(String durationTime) { this.durationTime = durationTime; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}


