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
- `Get_video_information.py`      bilibili视频信息获取程序，保存至mysql，需要请按代码内容创建数据表
- `generate_directory_tree.py`    用于根据文件内容生成树状文件结构目录
- `migrate_bilibili_to_schema.py` 用于将mysql导出的sql文件内容移植到H2可用的sql文件中(schema.sql)
- `高仿真原型-视频播放器.zip`      作业：高仿真原型

### 📁 文件夹
- `AISpringBoot`                 作业：SpringBoot架构的项目，包含了作业内容：显示学号姓名班级等
- `HTML源码截图`                  作业：用来练手的示例的HTML源码截图
- `HTML演示图片`                  作业：用来练手的示例的HTML效果截图
- `HTML`                         作业：HTML编辑教学时用于练手的示例
- `需求文档`                      作业：BRD/MRD/PRD文档

## 📁 项目文件结构
```bash
├── 📁 AISpringBoot/
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
│   │           │   └── 🌐 index.html
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
│   │   │   │   └── 🌐 index.html
│   │   │   │
│   │   │   ├── 📄 application.properties
│   │   │   ├── 📄 bilibili_videos.sql
│   │   │   ├── 📄 data.sql
│   │   │   └── 📄 schema.sql
│   │   │
│   │   ├── 📁 generated-sources/
│   │   │   └── 📁 annotations/

│   │   │
│   │   ├── 📁 maven-archiver/
│   │   │   └── 📄 pom.properties
│   │   │
│   │   ├── 📁 maven-status/
│   │   │   └── 📁 maven-compiler-plugin/
│   │   │       └── 📁 compile/
│   │   │           └── 📁 default-compile/
│   │   │               ├── 📄 createdFiles.lst
│   │   │               └── 📄 inputFiles.lst
│   │   │
│   │   ├── 📄 demo-0.0.1-SNAPSHOT.jar
│   │   └── 📄 demo-0.0.1-SNAPSHOT.jar.original
│   │
│   └── 📄 pom.xml
│
├── 📁 HTML/
│   ├── 🌐 QingPingLe.html
│   ├── 🌐 RenWuJianJie.html
│   ├── 🌐 WangLuShan.html
│   ├── 🌐 wqp.html
│   ├── 🖼️ wqp.jpg
│   └── 🌐 wqpjj.html
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
├── 📁 需求文档/
│   ├── 📄 BRD.docx
│   ├── 📄 MRD.docx
│   └── 📄 PRD.docx
│
├── 🐍 Get_video_information.py
├── 📄 README.md
├── 🐍 generate_directory_tree.py
├── 🐍 migrate_bilibili_to_schema.py
└── 📄 高仿真原型-视频播放器.zip
