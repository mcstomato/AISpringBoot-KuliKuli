package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Configuration
public class DataMigrationRunner {

    @Bean
    public CommandLineRunner migrateBilibiliVideos(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bilibili_videos", Integer.class);
                if (count != null && count > 0) {
                    return;
                }
            } catch (Exception ignored) {
                // table may not exist yet; schema.sql will create it before data.sql, and this runner runs afterwards
            }

            try {
                ClassPathResource sqlRes = new ClassPathResource("bilibili_videos.sql");
                if (!sqlRes.exists()) {
                    return;
                }
                String all = StreamUtils.copyToString(sqlRes.getInputStream(), StandardCharsets.UTF_8);

                // 提取 INSERT 段（包含多值插入）
                int idx = all.indexOf("INSERT INTO `bilibili_videos`");
                if (idx < 0) {
                    idx = all.indexOf("INSERT INTO bilibili_videos");
                }
                if (idx < 0) {
                    return;
                }
                int end = all.indexOf(";", idx);
                if (end < 0) {
                    end = all.length();
                }
                String insertSql = all.substring(idx, end + 1)
                        .replace("\r", "")
                        .replace("\\`", "`");

                if (insertSql.trim().isEmpty()) {
                    return;
                }

                // 执行导入（H2 已开启 MODE=MYSQL，支持反引号与多值插入）
                jdbcTemplate.execute(insertSql);
            } catch (Exception ignored) {
                // 忽略导入失败，不影响应用启动；可在控制台手动导入
            }
        };
    }
}


