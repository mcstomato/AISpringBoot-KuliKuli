import re
from pathlib import Path


def extract_values_from_mysql_dump(mysql_sql_path: Path) -> str:
    text = mysql_sql_path.read_text(encoding="utf-8")
    # Find the single INSERT line: INSERT INTO `bilibili_videos` VALUES (...);
    m = re.search(r"INSERT\s+INTO\s+`bilibili_videos`\s+VALUES\s*(\(.*?\));", text, re.DOTALL)
    if not m:
        raise RuntimeError("未找到 INSERT INTO `bilibili_videos` VALUES (...) 语句")
    values_block = m.group(1)
    # Optional pretty printing: split tuples to multiple lines
    pretty = values_block.replace("),(", "),\n(")
    return pretty


def replace_schema_insert(schema_sql_path: Path, new_values_block: str) -> None:
    schema_text = schema_sql_path.read_text(encoding="utf-8")

    # Match existing H2 insert block from header to terminating semicolon
    header_pattern = (
        r"INSERT\s+INTO\s+bilibili_videos\s*\(\s*" 
        r"id,\s*title,\s*up_name,\s*up_face_url,\s*view_count,\s*danmaku_count,\s*duration_time,\s*"
        r"aid,\s*bvid,\s*cid,\s*publish_time,\s*cover_url,\s*video_url,\s*embed_url,\s*fetch_time,\s*created_at\s*\)\s*VALUES"
    )

    insert_regex = re.compile(
        header_pattern + r"\s*(\(.*?\));",
        re.DOTALL,
    )

    new_block = (
        "INSERT INTO bilibili_videos (\n"
        "  id, title, up_name, up_face_url, view_count, danmaku_count, duration_time,\n"
        "  aid, bvid, cid, publish_time, cover_url, video_url, embed_url, fetch_time, created_at\n"
        ") VALUES\n" + new_values_block + ";"
    )

    if insert_regex.search(schema_text):
        updated = insert_regex.sub(lambda _: new_block, schema_text)
    else:
        # If not found, append after table definitions as a fallback
        updated = schema_text.rstrip() + "\n\n" + new_block + "\n"

    schema_sql_path.write_text(updated, encoding="utf-8")


def main():
    repo_root = Path(__file__).parent
    mysql_sql = repo_root / "AISpringBoot" / "src" / "main" / "resources" / "bilibili_videos.sql"
    schema_sql = repo_root / "AISpringBoot" / "src" / "main" / "resources" / "schema.sql"

    values_block = extract_values_from_mysql_dump(mysql_sql)
    replace_schema_insert(schema_sql, values_block)
    print("已更新 schema.sql 中的 bilibili_videos INSERT 段。")


if __name__ == "__main__":
    main()



