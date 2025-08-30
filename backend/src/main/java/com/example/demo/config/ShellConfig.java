package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Spring Shell 配置类
 * 用于配置Shell的基本设置和提供帮助信息
 */
@Configuration
public class ShellConfig {

    @Bean
    public String shellBanner() {
        return "==================================================\n" +
               "                    视频分享平台管理系统                        \n" +
               "                    Video Sharing Platform                    \n" +
               "==================================================\n" +
               "  输入 'help' 查看所有可用命令                                 \n" +
               "  输入 'help <命令名>' 查看具体命令用法                        \n" +
               "  输入 'stop' 停止应用程序                                   \n" +
               "==================================================";
    }
}
