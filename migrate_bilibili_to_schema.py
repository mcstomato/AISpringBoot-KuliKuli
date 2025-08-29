#!/usr/bin/env python3
"""
将 MySQL dump 中的 bilibili_videos 数据 INSERT 片段，移植/写入到 H2 用的 schema.sql

用法：
  python migrate_bilibili_to_schema.py

行为：
  - 自动在以下候选路径中查找来源与目标文件（按顺序优先匹配）：
      源数据：
        - backend/src/main/resources/bilibili_videos.sql
        - AISpringBoot/src/main/resources/bilibili_videos.sql
      目标 schema：
        - backend/src/main/resources/schema.sql
        - AISpringBoot/src/main/resources/schema.sql
  - 兼容多条 INSERT 语句、可选库名前缀/反引号/列清单
  - 将所有 VALUES 元组合并为一段，写入到 schema.sql 中固定列顺序的 INSERT 块
  - 若目标中已存在同类 INSERT 块会覆盖；否则会追加到文件末尾
"""

from __future__ import annotations

import re
from pathlib import Path
from typing import List, Tuple


def _read_text_with_fallback(p: Path) -> str:
    for enc in ("utf-8", "utf-8-sig", "gb18030", "latin-1"):
        try:
            return p.read_text(encoding=enc)
        except Exception:
            continue
    try:
        return p.read_bytes().decode("utf-8", errors="ignore")
    except Exception as e:
        raise RuntimeError(f"无法读取文件（编码不兼容）：{p}") from e


def extract_values_blocks(mysql_sql_path: Path) -> List[str]:
    """从 MySQL dump 文本中提取所有 bilibili_videos 的 VALUES 元组块。

    兼容写法：
      - INSERT INTO `bilibili_videos` VALUES (...);
      - INSERT INTO db.`bilibili_videos` VALUES (...);
      - INSERT INTO bilibili_videos (col,...) VALUES (...);
      - 多条 INSERT，将所有 VALUES(...) 块收集返回
    """
    text = _read_text_with_fallback(mysql_sql_path)

    insert_pattern = re.compile(
        r"INSERT\s+INTO\s+"           # INSERT INTO
        r"(?:`?\w+`?\.)?"             # 可选 schema 前缀 db.
        r"`?bilibili_videos`?\s*"      # 表名
        r"(?:\([^;]*?\))?\s*"        # 可选列清单
        r"VALUES\s*(\([\s\S]*?\))\s*;",  # 捕获 VALUES 后首个元组到分号
        re.IGNORECASE,
    )

    blocks = [m.group(1) for m in insert_pattern.finditer(text)]
    if not blocks:
        # Fallback：宽松提取，从 INSERT INTO ... VALUES 起始到下一个分隔标记
        start_match = re.search(
            r"INSERT\s+INTO\s+(?:`?\w+`?\.)?`?bilibili_videos`?(?:\([^)]*\))?\s+VALUES",
            text,
            re.IGNORECASE,
        )
        if not start_match:
            raise RuntimeError("未找到 INSERT INTO bilibili_videos VALUES (...) 语句，请检查源 SQL。")
        start = start_match.end()
        # 可能的结束标记：分号；或 MySQL dump 的 ENABLE KEYS/UNLOCK TABLES 之前
        tail = text[start:]
        end_candidates = []
        for pat in (r";", r"/\*!40000 ALTER TABLE\s+`?bilibili_videos`?\s+ENABLE KEYS\s*\*/\s*;", r"UNLOCK\s+TABLES\s*;"):
            m_end = re.search(pat, tail, re.IGNORECASE)
            if m_end:
                end_candidates.append(m_end.start())
        end = min(end_candidates) if end_candidates else len(tail)
        raw_values = tail[:end]
        # 去掉前导空白与可能的注释，规范分隔
        raw_values = raw_values.strip()
        # 去掉结尾可能的注释/多余内容
        raw_values = re.sub(r"/\*![0-9]{5}.*?\*/", "", raw_values, flags=re.DOTALL)
        # 去掉行尾逗号/空白
        raw_values = raw_values.rstrip()
        # 规范 ") , (" => 换行
        normalized_one = re.sub(r"\)\s*,\s*\(", "),\n(", raw_values)
        # 若起始不是 '(' 则尝试从第一个 '(' 开始
        first_paren = normalized_one.find('(')
        if first_paren > 0:
            normalized_one = normalized_one[first_paren:]
        blocks = [normalized_one]

    # 规范化每个 block 内部的分隔：") , (" => "),\n("
    normalized = [re.sub(r"\)\s*,\s*\(", "),\n(", b) for b in blocks]
    return normalized


def build_h2_insert_block(values_blocks: List[str]) -> str:
    """将多个 VALUES 元组块合并，并构建 H2 友好的 INSERT 语句块。"""
    merged_values = ",\n".join(values_blocks)
    header = (
        "INSERT INTO bilibili_videos (\n"
        "  id, title, up_name, up_face_url, view_count, danmaku_count, duration_time,\n"
        "  aid, bvid, cid, publish_time, cover_url, video_url, embed_url, fetch_time, created_at\n"
        ") VALUES\n"
    )
    return f"{header}{merged_values};"


def write_into_schema(schema_sql_path: Path, new_insert_block: str) -> None:
    schema_text = schema_sql_path.read_text(encoding="utf-8")

    header_pattern = (
        r"INSERT\s+INTO\s+bilibili_videos\s*\(\s*"
        r"id,\s*title,\s*up_name,\s*up_face_url,\s*view_count,\s*danmaku_count,\s*duration_time,\s*"
        r"aid,\s*bvid,\s*cid,\s*publish_time,\s*cover_url,\s*video_url,\s*embed_url,\s*fetch_time,\s*created_at\s*\)\s*VALUES"
    )
    insert_regex = re.compile(header_pattern + r"\s*(\(.*?\));", re.DOTALL)

    if insert_regex.search(schema_text):
        updated = insert_regex.sub(lambda _: new_insert_block, schema_text)
    else:
        updated = schema_text.rstrip() + "\n\n" + new_insert_block + "\n"

    schema_sql_path.write_text(updated, encoding="utf-8")


def resolve_paths() -> Tuple[Path, Path]:
    repo_root = Path(__file__).parent
    mysql_candidates = [
        repo_root / "backend" / "src" / "main" / "resources" / "bilibili_videos.sql",
        repo_root / "AISpringBoot" / "src" / "main" / "resources" / "bilibili_videos.sql",
    ]
    schema_candidates = [
        repo_root / "backend" / "src" / "main" / "resources" / "schema.sql",
        repo_root / "AISpringBoot" / "src" / "main" / "resources" / "schema.sql",
    ]
    mysql_sql = next((p for p in mysql_candidates if p.exists()), None)
    schema_sql = next((p for p in schema_candidates if p.exists()), None)
    if not mysql_sql:
        raise FileNotFoundError("未找到 bilibili_videos.sql（backend/… 或 AISpringBoot/…），请检查路径。")
    if not schema_sql:
        raise FileNotFoundError("未找到 schema.sql（backend/… 或 AISpringBoot/…），请检查路径。")
    return mysql_sql, schema_sql


def main() -> None:
    mysql_sql, schema_sql = resolve_paths()
    blocks = extract_values_blocks(mysql_sql)
    new_insert = build_h2_insert_block(blocks)
    write_into_schema(schema_sql, new_insert)
    print(f"✅ 已更新 {schema_sql} 中的 bilibili_videos INSERT 段（共 {len(blocks)} 个 INSERT 合并）。")


if __name__ == "__main__":
    main()


