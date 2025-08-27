#!/usr/bin/env python3
"""
自动生成目录树并更新 README.md
运行: python generate_directory_tree.py
"""

import os
import re
from pathlib import Path

def generate_tree(directory, ignore_dirs=[".git", "__pycache__", "node_modules"], prefix=""):
    """生成目录树字符串"""
    items = []
    try:
        entries = sorted(os.listdir(directory))
    except:
        return ""
    
    for index, entry in enumerate(entries):
        if entry in ignore_dirs:
            continue
            
        path = os.path.join(directory, entry)
        is_last = index == len(entries) - 1
        
        if os.path.isdir(path):
            items.append(f"{prefix}{'└── ' if is_last else '├── '}📁 {entry}/")
            extension = "    " if is_last else "│   "
            items.append(generate_tree(path, ignore_dirs, prefix + extension))
        else:
            # 为不同文件类型添加不同图标
            icons = {
                '.md': '📄', '.py': '🐍', '.js': '📜', '.html': '🌐',
                '.css': '🎨', '.json': '📋', '.txt': '📝', '.jpg': '🖼️',
                '.png': '🖼️', '.gitignore': '👁️'
            }
            icon = icons.get(Path(entry).suffix, '📄')
            items.append(f"{prefix}{'└── ' if is_last else '├── '}{icon} {entry}")
    
    return "\n".join(items)

def remove_directory_section():
    """删除 ## 📁 项目文件结构 前面两行和该行开始的所有后续内容"""
    readme_path = "README.md"
    
    if not os.path.exists(readme_path):
        print("❌ README.md 文件不存在")
        return False
    
    # 读取现有 README 内容（按行读取）
    with open(readme_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    # 查找 ## 📁 项目文件结构 的行号
    target_line_index = -1
    for i, line in enumerate(lines):
        if '## 📁 项目文件结构' in line:
            target_line_index = i
            break
    
    if target_line_index != -1:
        # 计算要删除的起始行（前面两行）
        start_delete_index = max(0, target_line_index - 2)
        
        # 只保留起始删除行之前的内容
        new_content = ''.join(lines[:start_delete_index])
        
        # 写入新内容
        with open(readme_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        
        print(f"✅ 已成功删除第 {start_delete_index + 1} 行开始的所有内容（包括目标行前两行）")
        return True
    else:
        print("ℹ️  未找到 '## 📁 项目文件结构' 部分")
        return False

def update_readme():
    """更新 README.md 文件"""
    remove_directory_section()
    readme_path = "README.md"
    
    # 生成目录树
    tree_content = generate_tree(".", ignore_dirs=[".git", "__pycache__", "node_modules", ".github"])
    
    # 构建新的目录树部分
    new_tree_section = f"""## 📁 项目文件结构

{tree_content}
"""
    # 读取现有 README
    if os.path.exists(readme_path):
        with open(readme_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 使用正则表达式替换目录树部分
        pattern = r'## 📁 项目文件结构.*?```.*?```'
        if re.search(pattern, content, re.DOTALL):
            new_content = re.sub(pattern, new_tree_section, content, flags=re.DOTALL)
        else:
            # 如果不存在，添加到文件末尾
            new_content = content + "\n\n" + new_tree_section
    else:
        new_content = f"""# 项目名称

{new_tree_section}
"""
    
    # 写入更新后的内容
    with open(readme_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print("✅ README.md 已更新！")

if __name__ == "__main__":
    update_readme()