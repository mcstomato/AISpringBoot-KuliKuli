## 🎯 高保真原型设计分工

| 模块 | 负责人 | 进度 |
|:----|:------|:----|
| 🧭 导航菜单布局 | 陈家林 | 🔄 完成 |
| 🔐 登录页面设计 | 陈家林 | 🔄 完成 |
| 🎬 影视页面设计 | 陈家林 | 🔄 完成 |
| 📺 直播页面设计 | 陈家林 | 🔄 完成 |
| 🎨 动画页面设计 | 田坤 | 🔄 完成 |
| ▶️ 播放页面设计 | 田坤 | 🔄 完成 |
| 🔥 热门页面设计 | 田坤 | 🔄 完成 |
| 💫 推荐页面设计 | 田坤 | 🔄 完成 |
| ✨ 动效制作 | 田坤 | 🔄 完成 |
| 🔗 跳转制作 | 田坤 | 🔄 完成 |


## ✨ 本级文件说明

### 📄 文件
| 文件/文件夹名                   | 说明                                                         |
|--------------------------------|--------------------------------------------------------------|
| `Get_video_information_hot.py`     | bilibili视频信息获取程序，保存至mysql，需要请按代码内容创建数据表 |
| `Get_video_information_ranking.py`     | bilibili视频信息获取程序，保存至mysql，需要请按代码内容创建数据表 |
| `generate_directory_tree.py`   | 用于根据文件内容生成树状文件结构目录                           |
| `migrate_bilibili_to_schema.py`| 用于将mysql导出的sql文件内容移植到H2可用的sql文件中(schema.sql) |
| `产品战略文档.docx`     | 作业：BRD MRD PRD | 
| `导出接口文档.html`     | 作业：apifox软件项目导出内容| 
| `apifox访问url.txt`     | 作业：项目分享至mock的链接| 
| `原型远程访问url.txt`     | 作业：高保真原型访问链接| 


### 📁 文件夹
| 文件/文件夹名     | 说明                                                         |
|------------------|--------------------------------------------------------------|
| `backend`   | 作业：SpringBoot架构的项目，包含了作业内容：显示学号姓名班级等 该文件是项目后端内容|
| `frontend`   | 作业：SpringBoot架构的项目，包含了作业内容：显示学号姓名班级等 该文件是前端静态网页内容|
| `HTML源码截图`    | 作业：用来练手的示例的HTML源码截图                           |
| `HTML演示图片`    | 作业：用来练手的示例的HTML效果截图                           |
| `HTML`           | 作业：HTML编辑教学时用于练手的示例                           |


## 📁 项目文件结构
```bash
├── 📁 backend/
│   ├── 📁 data/
│   │   ├── 📄 demo.mv.db
│   │   └── 📄 demo.trace.db
│   │
│   ├── 📁 src/
│   │   └── 📁 main/
│   │       ├── 📁 java/
│   │       │   └── 📁 com/
│   │       │       └── 📁 example/
│   │       │           └── 📁 demo/
│   │       │               ├── 📁 config/
│   │       │               │   ├── 📄 CorsConfig.java
│   │       │               │   └── 📄 DataMigrationRunner.java
│   │       │               │
│   │       │               ├── 📁 controller/
│   │       │               │   ├── 📄 BannerController.java
│   │       │               │   ├── 📄 ImageProxyController.java
│   │       │               │   ├── 📄 VideoController.java
│   │       │               │   └── 📄 WelcomeController.java
│   │       │               │
│   │       │               ├── 📁 dao/
│   │       │               │   ├── 📄 BannerMessageRepository.java
│   │       │               │   ├── 📄 BilibiliVideoRepository.java
│   │       │               │   └── 📄 PlaceholderDao.java
│   │       │               │
│   │       │               ├── 📁 model/
│   │       │               │   ├── 📄 BannerMessage.java
│   │       │               │   └── 📄 BilibiliVideo.java
│   │       │               │
│   │       │               ├── 📁 service/
│   │       │               │   └── 📄 WelcomeService.java
│   │       │               │
│   │       │               └── 📄 Application.java
│   │       │
│   │       └── 📁 resources/
│   │           ├── 📁 static/
│   │           │   ├── 🌐 index.html
│   │           │   ├── 🌐 play.html
│   │           │   ├── 🌐 test.html
│   │           │   └── 🌐 video.html
│   │           │
│   │           ├── 📄 application.properties
│   │           ├── 📄 bilibili_videos.sql
│   │           ├── 📄 data.sql
│   │           └── 📄 schema.sql
│   │
│   ├── 📁 target/
│   │   ├── 📁 classes/
│   │   │   ├── 📁 com/
│   │   │   │   └── 📁 example/
│   │   │   │       └── 📁 demo/
│   │   │   │           ├── 📁 config/
│   │   │   │           │   ├── 📄 CorsConfig.class
│   │   │   │           │   └── 📄 DataMigrationRunner.class
│   │   │   │           │
│   │   │   │           ├── 📁 controller/
│   │   │   │           │   ├── 📄 BannerController.class
│   │   │   │           │   ├── 📄 ImageProxyController.class
│   │   │   │           │   ├── 📄 VideoController.class
│   │   │   │           │   └── 📄 WelcomeController.class
│   │   │   │           │
│   │   │   │           ├── 📁 dao/
│   │   │   │           │   ├── 📄 BannerMessageRepository.class
│   │   │   │           │   ├── 📄 BilibiliVideoRepository.class
│   │   │   │           │   └── 📄 PlaceholderDao.class
│   │   │   │           │
│   │   │   │           ├── 📁 model/
│   │   │   │           │   ├── 📄 BannerMessage.class
│   │   │   │           │   └── 📄 BilibiliVideo.class
│   │   │   │           │
│   │   │   │           ├── 📁 service/
│   │   │   │           │   └── 📄 WelcomeService.class
│   │   │   │           │
│   │   │   │           └── 📄 Application.class
│   │   │   │
│   │   │   ├── 📁 static/
│   │   │   │   ├── 🌐 index.html
│   │   │   │   ├── 🌐 play.html
│   │   │   │   ├── 🌐 test.html
│   │   │   │   └── 🌐 video.html
│   │   │   │
│   │   │   ├── 📄 application.properties
│   │   │   ├── 📄 bilibili_videos.sql
│   │   │   ├── 📄 data.sql
│   │   │   └── 📄 schema.sql
│   │   │
│   │   ├── 📁 generated-sources/
│   │   │   └── 📁 annotations/

│   │   │
│   │   └── 📁 maven-status/
│   │       └── 📁 maven-compiler-plugin/
│   │           └── 📁 compile/
│   │               └── 📁 default-compile/
│   │                   ├── 📄 createdFiles.lst
│   │                   └── 📄 inputFiles.lst
│   │
│   ├── 📄 README.md
│   └── 📄 pom.xml
│
├── 📁 frontend/
│   ├── 🌐 index.html
│   ├── 🌐 play.html
│   ├── 🌐 test.html
│   ├── 🌐 video.html
│   └── 📋 package.json
│
├── 📁 HTML/
│   ├── 🌐 QingPingLe.html
│   ├── 🌐 RenWuJianJie.html
│   ├── 🌐 WangLuShan.html
│   ├── 🌐 wqp.html
│   ├── 🌐 wqpjj.html
│   └── 🖼️ wqp.jpg
│
├── 📁 HTML源码截图/
│   ├── 🖼️ 人物简介.png
│   ├── 🖼️ 吴某_对应核桃主页.png
│   ├── 🖼️ 吴某简介_对应核桃简介页.png
│   ├── 🖼️ 望庐山瀑布.png
│   └── 🖼️ 清平乐·年年雪里.png
│
├── 📁 HTML演示图片/
│   ├── 🖼️ 人物简介.png
│   ├── 🖼️ 吴某_对应核桃主页.png
│   ├── 🖼️ 吴某简介_对应核桃简介页.png
│   ├── 🖼️ 望庐山瀑布.png
│   └── 🖼️ 清平乐·年年雪里.png
│
├── 📄 产品战略文档.docx
├── 🌐 导出接口文档.html
├── 📄 README.md
├── 📝 apifox访问url.txt
├── 📝 原型远程访问url.txt
├── 📄 mysql导出bilibili_videos.sql.bat
├── 📄 运行项目.bat
├── 🐍 generate_directory_tree.py
├── 🐍 Get_video_information_hot.py
├── 🐍 Get_video_information_ranking.py
└── 🐍 migrate_bilibili_to_schema.py
