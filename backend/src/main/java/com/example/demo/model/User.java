package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_account", nullable = false, unique = true, length = 100)
    private String loginAccount;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "age")
    private Integer age;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "join_date")
    private java.time.LocalDate joinDate;

    @Column(name = "video_count")
    private Integer videoCount;

    @Column(name = "follower_count")
    private Integer followerCount;

    @Column(name = "following_count")
    private Integer followingCount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLoginAccount() { return loginAccount; }
    public void setLoginAccount(String loginAccount) { this.loginAccount = loginAccount; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public java.time.LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(java.time.LocalDate joinDate) { this.joinDate = joinDate; }

    public Integer getVideoCount() { return videoCount; }
    public void setVideoCount(Integer videoCount) { this.videoCount = videoCount; }

    public Integer getFollowerCount() { return followerCount; }
    public void setFollowerCount(Integer followerCount) { this.followerCount = followerCount; }

    public Integer getFollowingCount() { return followingCount; }
    public void setFollowingCount(Integer followingCount) { this.followingCount = followingCount; }
}


