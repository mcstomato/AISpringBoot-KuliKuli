package com.example.demo.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作命令类
 * 提供数据库相关的Shell命令操作
 */
@ShellComponent
public class DatabaseCommands {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    /**
     * 执行自定义SQL查询
     */
    @ShellMethod(value = "执行自定义SQL查询", key = "db query")
    public String executeQuery(
            @ShellOption(value = "sql", help = "SQL查询语句") String sql
    ) {
        try {
            // 安全检查：只允许执行SELECT查询语句
            String upperSql = sql.trim().toUpperCase();
            if (!upperSql.startsWith("SELECT")) {
                return "错误：出于安全考虑，只允许执行SELECT查询语句";
            }

            List<String> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                StringBuilder row = new StringBuilder();
                
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    row.append(value != null ? value : "NULL");
                    if (i < columnCount) {
                        row.append(" | ");
                    }
                }
                return row.toString();
            });

            if (results.isEmpty()) {
                return "查询结果为空";
            }

            // 获取列信息
            List<String> columns = new ArrayList<>();
            try {
                List<List<String>> columnResults = jdbcTemplate.query(sql, (rs, rowNum) -> {
                    if (rowNum == 0) { // 只处理第一行来获取列信息
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        List<String> cols = new ArrayList<>();
                        for (int i = 1; i <= columnCount; i++) {
                            cols.add(metaData.getColumnName(i));
                        }
                        return cols;
                    }
                    return null;
                });
                
                if (!columnResults.isEmpty() && columnResults.get(0) != null) {
                    columns = columnResults.get(0);
                }
            } catch (Exception e) {
                // 如果获取列信息失败，使用默认列名
                columns.add("Column");
            }

            StringBuilder result = new StringBuilder();
            result.append("查询结果：\n");
            
            // 绘制表头
            if (!columns.isEmpty()) {
                StringBuilder headerLine = new StringBuilder();
                StringBuilder separatorLine = new StringBuilder();
                
                for (String column : columns) {
                    headerLine.append("│ ").append(String.format("%-20s", column));
                    separatorLine.append("├────────────────────");
                }
                headerLine.append("│");
                separatorLine.append("┤");
                
                result.append(headerLine.toString()).append("\n");
                result.append(separatorLine.toString()).append("\n");
            }

            // 绘制数据行
            for (String row : results) {
                String[] values = row.split(" \\| ");
                StringBuilder dataLine = new StringBuilder();
                for (String value : values) {
                    dataLine.append("│ ").append(String.format("%-20s", value.length() > 18 ? value.substring(0, 15) + "..." : value));
                }
                dataLine.append("│");
                result.append(dataLine.toString()).append("\n");
            }

            // 绘制表尾
            StringBuilder footerLine = new StringBuilder();
            for (int i = 0; i < (columns != null ? columns.size() : 1); i++) {
                footerLine.append("└────────────────────");
            }
            footerLine.append("┘");
            result.append(footerLine.toString()).append("\n");
            
            result.append("查询到 ").append(results.size()).append(" 行数据");
            return result.toString();

        } catch (Exception e) {
            return "执行查询时出错: " + e.getMessage();
        }
    }

    /**
     * 查看数据库中的所有表
     */
    @ShellMethod(value = "查看数据库中的所有表", key = "db tables")
    public String showTables() {
        try {
            List<String> tables = jdbcTemplate.queryForList(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", 
                String.class
            );

            if (tables.isEmpty()) {
                return "数据库中没有表";
            }

            StringBuilder result = new StringBuilder();
            result.append("数据库表列表：\n");
            result.append("┌─────────────────────────────────────────────────────────────────────────────────┐\n");
            result.append("│                                   表名                                          │\n");
            result.append("├─────────────────────────────────────────────────────────────────────────────────┤\n");

            for (String table : tables) {
                result.append(String.format("│ %-70s │\n", table));
            }
            result.append("└─────────────────────────────────────────────────────────────────────────────────┘\n");
            result.append("总计: ").append(tables.size()).append(" 个表");
            return result.toString();

        } catch (Exception e) {
            return "获取表列表时出错: " + e.getMessage();
        }
    }

    /**
     * 查看指定表的结构
     */
    @ShellMethod(value = "查看指定表的结构", key = "db describe")
    public String describeTable(
            @ShellOption(value = "table", help = "表名", defaultValue = ShellOption.NULL) String tableName
    ) {
        try {
            List<String> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? AND TABLE_SCHEMA = 'PUBLIC' ORDER BY ORDINAL_POSITION", 
                String.class, tableName
            );

            if (columns.isEmpty()) {
                return "表 " + tableName + " 不存在或没有列";
            }

            StringBuilder result = new StringBuilder();
            result.append("表 ").append(tableName).append(" 的结构：\n");
            result.append("┌─────────────────┬─────────────────┬──────────────┬─────────────────┐\n");
            result.append("│     列名        │     数据类型     │   是否为空    │     默认值      │\n");
            result.append("├─────────────────┼─────────────────┼──────────────┼─────────────────┤\n");

            for (String column : columns) {
                String[] parts = column.split(",");
                if (parts.length >= 4) {
                    result.append(String.format("│ %-15s │ %-15s │ %-12s │ %-15s │\n", 
                        parts[0], parts[1], parts[2], parts[3]));
                }
            }
            result.append("└─────────────────┴─────────────────┴──────────────┴─────────────────┘\n");
            return result.toString();

        } catch (Exception e) {
            return "获取表结构时出错: " + e.getMessage();
        }
    }

    /**
     * 查看数据库状态
     */
    @ShellMethod(value = "查看数据库状态", key = "db status")
    public String showDatabaseStatus() {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            
            return String.format("数据库状态信息：\n" +
                    "==================================================\n" +
                    " 数据库产品名称: %s\n" +
                    " 数据库版本:     %s\n" +
                    " 驱动名称:       %s\n" +
                    " 驱动版本:       %s\n" +
                    " URL:           %s\n" +
                    " 用户名:         %s\n" +
                    "==================================================",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion(),
                    metaData.getDriverName(),
                    metaData.getDriverVersion(),
                    metaData.getURL(),
                    metaData.getUserName()
            );

        } catch (Exception e) {
            return "获取数据库状态时出错: " + e.getMessage();
        }
    }

    /**
     * 清空指定表的数据
     */
    @ShellMethod(value = "清空指定表的数据（危险操作）", key = "db clear")
    public String clearTable(
            @ShellOption(value = "table", help = "表名", defaultValue = ShellOption.NULL) String tableName
    ) {
        try {
            // 安全检查：只允许清空特定表
            String lowerTableName = tableName.toLowerCase();
            if (!lowerTableName.equals("users") && !lowerTableName.equals("bilibili_videos") && 
                !lowerTableName.equals("banner_message")) {
                return "错误：出于安全考虑，只能清空 users、bilibili_videos 或 banner_message 表";
            }

            int affectedRows = jdbcTemplate.update("DELETE FROM " + tableName);
            return "表 " + tableName + " 清空成功，删除了 " + affectedRows + " 行数据";

        } catch (Exception e) {
            return "清空表时出错: " + e.getMessage();
        }
    }
}
