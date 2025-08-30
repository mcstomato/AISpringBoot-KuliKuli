package com.example.demo.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import java.lang.management.ManagementFactory;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统管理命令类
 * 提供系统相关的Shell命令操作
 */
@ShellComponent
public class SystemCommands {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 显示帮助信息
     */
    @ShellMethod(value = "显示帮助信息", key = "help")
    public String showHelp(
            @ShellOption(value = "command", help = "具体命令名") String command
    ) {
        if (command != null) {
            return getCommandHelp(command);
        }

        return "==================================================\n" +
               "                    视频分享平台管理系统                        \n" +
               "                    Video Sharing Platform                    \n" +
               "==================================================\n" +
               "  系统命令：\n" +
               "    help                    - 显示帮助信息\n" +
               "    status                  - 显示系统状态\n" +
               "    clear                   - 清屏\n" +
               "    silent                  - 日志管理说明\n" +
               "    stop                    - 停止应用程序\n" +
               "    exit                    - 退出应用程序\n" +
               "    quit                    - 退出应用程序（别名）\n" +
               "\n" +
               "  用户管理：\n" +
               "    users list              - 查看所有用户\n" +
               "    users show <id>         - 查看指定用户详情\n" +
               "    users add               - 添加新用户\n" +
               "    users update <id>       - 更新用户信息\n" +
               "    users delete <id>       - 删除用户\n" +
               "    users search <keyword>  - 搜索用户\n" +
               "\n" +
               "  视频管理：\n" +
               "    videos list             - 查看所有视频\n" +
               "    videos show <id>        - 查看指定视频详情\n" +
               "    videos search <keyword> - 搜索视频\n" +
               "    videos add              - 添加新视频\n" +
               "    videos delete <id>      - 删除视频\n" +
               "    videos hot [limit]      - 查看热门视频\n" +
               "    videos stats            - 视频统计信息\n" +
               "\n" +
               "  数据库操作：\n" +
               "    db query <sql>          - 执行SQL查询\n" +
               "    db tables               - 查看所有表\n" +
               "    db describe <table>     - 查看表结构\n" +
               "    db status               - 数据库状态\n" +
               "    db clear <table>        - 清空表数据\n" +
               "\n" +
               "  提示：\n" +
               "  - 输入 'help <命令名>' 查看具体命令用法\n" +
               "  - 输入 'exit' 或 'quit' 退出应用程序\n" +
               "==================================================";
    }

    /**
     * 显示系统状态
     */
    @ShellMethod(value = "显示系统状态信息", key = "status")
    public String showStatus() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return String.format("系统状态信息：\n" +
                "==================================================\n" +
                " 当前时间         │ %-43s │\n" +
                " 系统版本         │ %-43s │\n" +
                " Java版本        │ %-43s │\n" +
                " 内存使用         │ %-43s │\n" +
                " 线程数量         │ %-43s │\n" +
                " 运行时长         │ %-43s │\n" +
                "==================================================\n" +
                "\n" +
                "提示：\n" +
                "- 输入 'help' 查看所有可用命令\n" +
                "- 输入 'help <命令名>' 查看具体命令用法\n" +
                "- 输入 'exit' 或 'quit' 退出Shell",
                now.format(formatter),
                "Spring Boot 2.7.18",
                System.getProperty("java.version"),
                getMemoryUsage(),
                Thread.activeCount(),
                "运行中"
        );
    }

    /**
     * 清屏命令
     */
    @ShellMethod(value = "清屏", key = "clear")
    public String clearScreen() {
        // 输出多个换行符来模拟清屏
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("\n");
        }
        return sb.append("屏幕已清空，可以继续输入命令\n").toString();
    }

    /**
     * 静默模式说明
     */
    @ShellMethod(value = "静默模式说明", key = "silent")
    public String silentModeInfo() {
        return "日志管理说明：\n" +
               "==================================================\n" +
               "1. 完整日志输出已恢复，所有系统信息都会正常显示\n" +
               "2. 如果日志信息干扰了命令输入，可以使用以下方法：\n" +
               "   - 使用 'clear' 命令清除屏幕上的日志信息\n" +
               "   - 等待日志输出完成后再输入命令\n" +
               "   - 日志输出时不影响命令执行\n" +
               "3. 提示：日志信息有助于调试和监控系统状态\n" +
               "==================================================";
    }

    /**
     * 停止应用程序
     */
    @ShellMethod(value = "停止应用程序", key = "stop")
    public String stopApplication() {
        // 使用Spring Boot的方式优雅关闭应用程序
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 等待1秒，让用户看到停止消息
                // 使用Spring Boot的方式优雅关闭
                int exitCode = SpringApplication.exit(applicationContext, () -> 0);
                System.exit(exitCode);
            } catch (Exception e) {
                // 如果优雅关闭失败，使用强制退出
                System.exit(0);
            }
        }).start();
        
        return "正在停止应用程序...";
    }

    /**
     * 退出应用程序（覆盖默认exit命令）
     */
    @ShellMethod(value = "退出应用程序", key = "exit", group = "System")
    public String exitApplication() {
        return stopApplication();
    }

    /**
     * 退出应用程序（别名）
     */
    @ShellMethod(value = "退出应用程序", key = "quit")
    public String quitApplication() {
        return stopApplication();
    }

    /**
     * 获取内存使用情况
     */
    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        
        return String.format("%.1f MB / %.1f MB (%.1f%%)", 
                usedMemory / 1024.0 / 1024.0,
                maxMemory / 1024.0 / 1024.0,
                (usedMemory * 100.0) / maxMemory);
    }

    /**
     * 获取具体命令的帮助信息
     */
    private String getCommandHelp(String command) {
        switch (command.toLowerCase()) {
            case "help":
                return "help [command] - 显示帮助信息\n" +
                       "  参数：command - 具体命令名（可选）\n" +
                       "  示例：help users list";
            case "status":
                return "status - 显示系统状态信息\n" +
                       "  显示当前时间、系统版本、内存使用等信息";
            case "clear":
                return "clear - 清屏\n" +
                       "  清除当前屏幕显示，移除日志干扰";
            case "silent":
                return "silent - 日志管理说明\n" +
                       "  查看日志输出管理方法";
            case "stop":
                return "stop - 停止应用程序\n" +
                       "  优雅关闭Spring Boot应用程序";
            case "exit":
                return "exit - 退出应用程序\n" +
                       "  优雅关闭Spring Boot应用程序";
            case "quit":
                return "quit - 退出应用程序（别名）\n" +
                       "  优雅关闭Spring Boot应用程序";
            case "users":
                return "用户管理命令：\n" +
                       "  users list              - 查看所有用户\n" +
                       "  users show <id>         - 查看指定用户详情\n" +
                       "  users add               - 添加新用户\n" +
                       "  users update <id>       - 更新用户信息\n" +
                       "  users delete <id>       - 删除用户\n" +
                       "  users search <keyword>  - 搜索用户";
            case "videos":
                return "视频管理命令：\n" +
                       "  videos list             - 查看所有视频\n" +
                       "  videos show <id>        - 查看指定视频详情\n" +
                       "  videos search <keyword> - 搜索视频\n" +
                       "  videos add              - 添加新视频\n" +
                       "  videos delete <id>      - 删除视频\n" +
                       "  videos hot [limit]      - 查看热门视频\n" +
                       "  videos stats            - 视频统计信息";
            case "db":
                return "数据库操作命令：\n" +
                       "  db query <sql>          - 执行SQL查询\n" +
                       "  db tables               - 查看所有表\n" +
                       "  db describe <table>     - 查看表结构\n" +
                       "  db status               - 数据库状态\n" +
                       "  db clear <table>        - 清空表数据";
            default:
                return "未知命令: " + command + "\n" +
                       "输入 'help' 查看所有可用命令";
        }
    }
}
