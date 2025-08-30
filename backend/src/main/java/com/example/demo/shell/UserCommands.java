package com.example.demo.shell;

import com.example.demo.dao.UserRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户管理命令类
 * 提供用户相关的Shell命令操作
 */
@ShellComponent
public class UserCommands {

    @Autowired
    private UserRepository userRepository;

    /**
     * 查看所有用户
     */
    @ShellMethod(value = "查看所有用户列表", key = "users list")
    public String listUsers() {
        List<User> users = userRepository.findAll();
        
        if (users.isEmpty()) {
            return "暂无用户数据";
        }

        StringBuilder result = new StringBuilder();
        result.append("用户列表：\n");
        result.append("┌─────────┬─────────────────┬─────────────────┬──────────────┬─────────────────┐\n");
        result.append("│   ID    │    登录账号     │      昵称       │     年龄     │   加入时间      │\n");
        result.append("├─────────┼─────────────────┼─────────────────┼──────────────┼─────────────────┤\n");

        for (User user : users) {
            String nickname = user.getNickname() != null ? user.getNickname() : "未知";
            String age = user.getAge() != null ? user.getAge().toString() : "未知";
            String joinDate = user.getJoinDate() != null ? user.getJoinDate().toString() : "未知";
            
            result.append(String.format("│ %7d │ %-15s │ %-15s │ %-12s │ %-15s │\n", 
                    user.getId(), 
                    user.getLoginAccount(), 
                    nickname,
                    age,
                    joinDate));
        }
        result.append("└─────────┴─────────────────┴─────────────────┴──────────────┴─────────────────┘\n");
        result.append("总计: ").append(users.size()).append(" 个用户");
        return result.toString();
    }

    /**
     * 查看指定用户详情
     */
    @ShellMethod(value = "查看指定用户详情", key = "users show")
    public String showUser(
            @ShellOption(value = "id", help = "用户ID", defaultValue = ShellOption.NULL) Integer id,
            @ShellOption(value = "account", help = "登录账号", defaultValue = ShellOption.NULL) String account
    ) {
        User user = null;
        
        if (id != null) {
            Optional<User> userOpt = userRepository.findById(id.longValue());
            if (userOpt.isPresent()) {
                user = userOpt.get();
            }
        } else if (account != null) {
            Optional<User> userOpt = userRepository.findByLoginAccount(account);
            if (userOpt.isPresent()) {
                user = userOpt.get();
            }
        } else {
            return "错误：请提供用户ID或登录账号";
        }

        if (user == null) {
            return "用户不存在";
        }

        return String.format("用户详情：\n" +
                "==================================================\n" +
                " ID:           %d\n" +
                " 登录账号:     %s\n" +
                " 昵称:         %s\n" +
                " 年龄:         %s\n" +
                " 个人简介:     %s\n" +
                " 头像URL:      %s\n" +
                " 加入时间:     %s\n" +
                " 视频数量:     %d\n" +
                " 粉丝数量:     %d\n" +
                " 关注数量:     %d\n" +
                "==================================================",
                user.getId(),
                user.getLoginAccount(),
                user.getNickname() != null ? user.getNickname() : "未知",
                user.getAge() != null ? user.getAge().toString() : "未知",
                user.getBio() != null ? user.getBio() : "未知",
                user.getAvatarUrl() != null ? user.getAvatarUrl() : "未知",
                user.getJoinDate() != null ? user.getJoinDate().toString() : "未知",
                user.getVideoCount() != null ? user.getVideoCount() : 0,
                user.getFollowerCount() != null ? user.getFollowerCount() : 0,
                user.getFollowingCount() != null ? user.getFollowingCount() : 0
        );
    }

    /**
     * 添加新用户
     */
    @ShellMethod(value = "添加新用户", key = "users add")
    public String addUser(
            @ShellOption(value = "account", help = "登录账号") String account,
            @ShellOption(value = "nickname", help = "昵称") String nickname,
            @ShellOption(value = "password", help = "密码") String password,
            @ShellOption(value = "age", help = "年龄") Integer age,
            @ShellOption(value = "bio", help = "个人简介") String bio,
            @ShellOption(value = "avatar", help = "头像URL") String avatarUrl
    ) {
        // 检查账号是否已存在
        if (userRepository.existsByLoginAccount(account)) {
            return "错误：登录账号已存在";
        }

        // 检查账号是否包含中文
        if (account.matches(".*[\\u4e00-\\u9fa5].*")) {
            return "错误：登录账号不能包含中文字符";
        }

        User user = new User();
        user.setLoginAccount(account);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setAge(age);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);
        user.setJoinDate(LocalDate.now());
        user.setVideoCount(0);
        user.setFollowerCount(0);
        user.setFollowingCount(0);

        User savedUser = userRepository.save(user);
        return "用户添加成功！ID: " + savedUser.getId();
    }

    /**
     * 更新用户信息
     */
    @ShellMethod(value = "更新用户信息", key = "users update")
    public String updateUser(
            @ShellOption(value = "id", help = "用户ID") Integer id,
            @ShellOption(value = "nickname", help = "昵称") String nickname,
            @ShellOption(value = "age", help = "年龄") Integer age,
            @ShellOption(value = "bio", help = "个人简介") String bio,
            @ShellOption(value = "avatar", help = "头像URL") String avatarUrl,
            @ShellOption(value = "videoCount", help = "视频数量") Integer videoCount,
            @ShellOption(value = "followerCount", help = "粉丝数量") Integer followerCount,
            @ShellOption(value = "followingCount", help = "关注数量") Integer followingCount
    ) {
        Optional<User> userOpt = userRepository.findById(id.longValue());
        if (!userOpt.isPresent()) {
            return "用户不存在";
        }

        User user = userOpt.get();
        
        if (nickname != null) user.setNickname(nickname);
        if (age != null) user.setAge(age);
        if (bio != null) user.setBio(bio);
        if (avatarUrl != null) user.setAvatarUrl(avatarUrl);
        if (videoCount != null) user.setVideoCount(videoCount);
        if (followerCount != null) user.setFollowerCount(followerCount);
        if (followingCount != null) user.setFollowingCount(followingCount);

        userRepository.save(user);
        return "用户信息更新成功！";
    }

    /**
     * 删除用户
     */
    @ShellMethod(value = "删除用户", key = "users delete")
    public String deleteUser(
            @ShellOption(value = "id", help = "用户ID", defaultValue = ShellOption.NULL) Integer id,
            @ShellOption(value = "account", help = "登录账号", defaultValue = ShellOption.NULL) String account
    ) {
        User user = null;
        
        if (id != null) {
            Optional<User> userOpt = userRepository.findById(id.longValue());
            if (userOpt.isPresent()) {
                user = userOpt.get();
            }
        } else if (account != null) {
            Optional<User> userOpt = userRepository.findByLoginAccount(account);
            if (userOpt.isPresent()) {
                user = userOpt.get();
            }
        } else {
            return "错误：请提供用户ID或登录账号";
        }

        if (user == null) {
            return "用户不存在";
        }

        userRepository.delete(user);
        return "用户删除成功！";
    }

    /**
     * 搜索用户
     */
    @ShellMethod(value = "搜索用户", key = "users search")
    public String searchUsers(
            @ShellOption(value = "keyword", help = "搜索关键词") String keyword
    ) {
        List<User> allUsers = userRepository.findAll();
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> 
                    (user.getLoginAccount() != null && user.getLoginAccount().toLowerCase().contains(keyword.toLowerCase())) ||
                    (user.getNickname() != null && user.getNickname().toLowerCase().contains(keyword.toLowerCase())) ||
                    (user.getBio() != null && user.getBio().toLowerCase().contains(keyword.toLowerCase()))
                )
                .collect(Collectors.toList());

        if (filteredUsers.isEmpty()) {
            return "未找到匹配的用户";
        }

        StringBuilder result = new StringBuilder();
        result.append("搜索结果：\n");
        result.append("┌─────────┬─────────────────┬─────────────────┬──────────────┬─────────────────┐\n");
        result.append("│   ID    │    登录账号     │      昵称       │     年龄     │   加入时间      │\n");
        result.append("├─────────┼─────────────────┼─────────────────┼──────────────┼─────────────────┤\n");

        for (User user : filteredUsers) {
            String nickname = user.getNickname() != null ? user.getNickname() : "未知";
            String age = user.getAge() != null ? user.getAge().toString() : "未知";
            String joinDate = user.getJoinDate() != null ? user.getJoinDate().toString() : "未知";
            
            result.append(String.format("│ %7d │ %-15s │ %-15s │ %-12s │ %-15s │\n", 
                    user.getId(), 
                    user.getLoginAccount(), 
                    nickname,
                    age,
                    joinDate));
        }
        result.append("└─────────┴─────────────────┴─────────────────┴──────────────┴─────────────────┘\n");
        result.append("找到: ").append(filteredUsers.size()).append(" 个用户");
        return result.toString();
    }
}
