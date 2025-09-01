INSERT INTO banner_message (content) VALUES ('我是黑大电子1班的田坤，我的学号是20222281');

-- 初始化用户数据（只在用户不存在时插入）
INSERT INTO users (login_account, nickname, password, avatar_url, age, bio, join_date, video_count, follower_count, following_count) 
SELECT 'user001', '小明', '123456', 'https://example.com/avatar/user001.png', 25, '热爱生活，分享快乐', '2024-01-15', 12, 156, 89
WHERE NOT EXISTS (SELECT 1 FROM users WHERE login_account = 'user001');

INSERT INTO users (login_account, nickname, password, avatar_url, age, bio, join_date, video_count, follower_count, following_count) 
SELECT 'user002', '小红', '123456', 'https://example.com/avatar/user002.png', 30, '技术爱好者，专注编程', '2024-03-20', 8, 89, 234
WHERE NOT EXISTS (SELECT 1 FROM users WHERE login_account = 'user002');
