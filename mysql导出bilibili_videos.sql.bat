mysqldump -uroot -p123456 --default-character-set=utf8mb4 --no-tablespaces --skip-set-charset bilibili_data bilibili_videos > backend\src\main\resources\bilibili_videos.sql

python migrate_bilibili_to_schema.py