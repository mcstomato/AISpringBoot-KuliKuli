package com.example.demo.controller;

import com.example.demo.dao.UserRepository;
import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String loginAccount = body.getOrDefault("login_account", "").trim();
        String nickname = body.getOrDefault("nickname", "").trim();
        String password = body.getOrDefault("password", "").trim();
        String confirmPassword = body.getOrDefault("confirm_password", "").trim();

        Map<String, Object> result = new HashMap<>();

        if (loginAccount.isEmpty()) {
            result.put("success", false);
            result.put("message", "登录账号不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        if (CHINESE_PATTERN.matcher(loginAccount).find()) {
            result.put("success", false);
            result.put("message", "请勿输入中文");
            return ResponseEntity.badRequest().body(result);
        }
        if (nickname.isEmpty()) {
            result.put("success", false);
            result.put("message", "昵称不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        if (password.isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        if (!password.equals(confirmPassword)) {
            result.put("success", false);
            result.put("message", "密码不一致");
            return ResponseEntity.badRequest().body(result);
        }
        if (userRepository.existsByLoginAccount(loginAccount)) {
            result.put("success", false);
            result.put("message", "账号已存在");
            return ResponseEntity.badRequest().body(result);
        }

        User user = new User();
        user.setLoginAccount(loginAccount);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setAvatarUrl(null);
        user.setJoinDate(LocalDate.now()); // 设置加入时间为当前日期

        userRepository.save(user);

        // 返回完整的用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("login_account", user.getLoginAccount());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar_url", user.getAvatarUrl());
        userInfo.put("bio", user.getBio());
        userInfo.put("age", user.getAge());
        userInfo.put("join_date", user.getJoinDate());
        userInfo.put("video_count", user.getVideoCount());
        userInfo.put("follower_count", user.getFollowerCount());
        userInfo.put("following_count", user.getFollowingCount());

        result.put("success", true);
        result.put("message", "注册成功");
        result.put("user", userInfo);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String loginAccount = body.getOrDefault("login_account", "").trim();
        String password = body.getOrDefault("password", "").trim();

        Map<String, Object> result = new HashMap<>();
        if (loginAccount.isEmpty() || password.isEmpty()) {
            result.put("success", false);
            result.put("message", "账号或密码不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        if (CHINESE_PATTERN.matcher(loginAccount).find()) {
            result.put("success", false);
            result.put("message", "请勿输入中文");
            return ResponseEntity.badRequest().body(result);
        }

        return userRepository.findByLoginAccount(loginAccount)
                .map(u -> {
                    if (!u.getPassword().equals(password)) {
                        Map<String, Object> r = new HashMap<>();
                        r.put("success", false);
                        r.put("message", "账号或密码错误");
                        return ResponseEntity.status(401).body(r);
                    }
                    Map<String, Object> r = new HashMap<>();
                    r.put("success", true);
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", u.getId());
                    user.put("login_account", u.getLoginAccount());
                    user.put("nickname", u.getNickname());
                    user.put("avatar_url", u.getAvatarUrl());
                    user.put("bio", u.getBio());
                    user.put("age", u.getAge());
                    user.put("join_date", u.getJoinDate());
                    user.put("video_count", u.getVideoCount());
                    user.put("follower_count", u.getFollowerCount());
                    user.put("following_count", u.getFollowingCount());
                    r.put("user", user);
                    return ResponseEntity.ok(r);
                })
                .orElseGet(() -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("success", false);
                    r.put("message", "账号或密码错误");
                    return ResponseEntity.status(401).body(r);
                });
    }

    @PostMapping("/check-status")
    public ResponseEntity<?> checkUserStatus(@RequestBody Map<String, String> body) {
        String loginAccount = body.getOrDefault("login_account", "").trim();
        
        Map<String, Object> result = new HashMap<>();
        
        if (loginAccount.isEmpty()) {
            result.put("success", false);
            result.put("message", "登录账号不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        
        return userRepository.findByLoginAccount(loginAccount)
                .map(user -> {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("login_account", user.getLoginAccount());
                    userInfo.put("nickname", user.getNickname());
                    userInfo.put("avatar_url", user.getAvatarUrl());
                    userInfo.put("bio", user.getBio());
                    userInfo.put("age", user.getAge());
                    userInfo.put("join_date", user.getJoinDate());
                    userInfo.put("video_count", user.getVideoCount());
                    userInfo.put("follower_count", user.getFollowerCount());
                    userInfo.put("following_count", user.getFollowingCount());
                    
                    result.put("success", true);
                    result.put("message", "用户存在");
                    result.put("user", userInfo);
                    return ResponseEntity.ok(result);
                })
                .orElseGet(() -> {
                    result.put("success", false);
                    result.put("message", "用户不存在");
                    return ResponseEntity.ok(result);
                });
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> body) {
        String nickname = body.getOrDefault("nickname", "").trim();
        String password = body.getOrDefault("password", "").trim();
        String bio = body.getOrDefault("bio", "").trim();
        String avatarUrl = body.getOrDefault("avatar_url", "").trim();
        String loginAccount = body.getOrDefault("login_account", "").trim();

        Map<String, Object> result = new HashMap<>();

        if (nickname.isEmpty()) {
            result.put("success", false);
            result.put("message", "昵称不能为空");
            return ResponseEntity.badRequest().body(result);
        }

        if (loginAccount.isEmpty()) {
            result.put("success", false);
            result.put("message", "登录账号不能为空");
            return ResponseEntity.badRequest().body(result);
        }

        return userRepository.findByLoginAccount(loginAccount)
                .map(user -> {
                    user.setNickname(nickname);
                    user.setBio(bio);
                    user.setAvatarUrl(avatarUrl.isEmpty() ? null : avatarUrl);
                    
                    if (!password.isEmpty()) {
                        if (password.length() < 6) {
                            result.put("success", false);
                            result.put("message", "密码长度至少6位");
                            return ResponseEntity.badRequest().body(result);
                        }
                        user.setPassword(password);
                    }

                    userRepository.save(user);

                    Map<String, Object> updatedUser = new HashMap<>();
                    updatedUser.put("id", user.getId());
                    updatedUser.put("login_account", user.getLoginAccount());
                    updatedUser.put("nickname", user.getNickname());
                    updatedUser.put("avatar_url", user.getAvatarUrl());
                    updatedUser.put("bio", user.getBio());
                    updatedUser.put("age", user.getAge());
                    updatedUser.put("join_date", user.getJoinDate());
                    updatedUser.put("video_count", user.getVideoCount());
                    updatedUser.put("follower_count", user.getFollowerCount());
                    updatedUser.put("following_count", user.getFollowingCount());

                    result.put("success", true);
                    result.put("message", "更新成功");
                    result.put("user", updatedUser);
                    return ResponseEntity.ok(result);
                })
                .orElseGet(() -> {
                    result.put("success", false);
                    result.put("message", "用户不存在");
                    return ResponseEntity.badRequest().body(result);
                });
    }
}


