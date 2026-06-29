# 🚀 项目在线演示

## 🌐 在线访问地址
**项目主页**: [http://60.205.198.37:80](http://60.205.198.37:80)（没钱续服务器了 已到期bushi）

> 💡 **提示**: 项目已成功部署至云服务器，支持在线访问和功能演示

> 💡 **提示**: 若图片无法加载请更换网络，已知黑龙江大学某实验室wifi无法正常加载图片

> 💡 **提示**: 强大的搜索功能已上线 您几乎可以在本项目中搜索到几乎所有视频

> 💡 **提示**: 当前服务器总时限三个月 期间若您发现无法访问主页 大概率是碰到我正在修BUG或者更新部署

> 💡 **皮一下**: (贫困大学生的服务器来自哪？ 当然是白嫖阿里云喽)

## ⚡ 放在最前面的提示

后端的详细使用说明在backend文件

若要进行运行请务必认真观看

运行项目.bat是需要整个项目文件才可以运行

一键启动.bat仅需要demo-0.0.1-SNAPSHOT.jar即可运行

一键启动会自动搜索附近的jar文件，不过还是最好放在同一级目录下

仅仅查看作业的效果强烈建议使用jar文件+一键启动！！！

因为这几天实习我基本都在高强度码代码(戳啦 其实是ai码代码)

搞得我已经忘了该项目哪些地方用了绝对路径....


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
| `运行项目.bat`     | 运行SpringBoot服务端(开发模式),需要编辑Maven地址| 
| `mysql导出bilibili_videos.sql.bat` | 用于将mysql数据库进行导出sql，并将mysql的sql文件信息移植进H2数据库中|
| `导出数据库：bilibili_videos.sql` | mysql导出视频数据|
| `导出数据库：data.sql` | 测试用户初始化以及横幅显示信息数据库|
| `导出数据库：schema.sql` | mysql转成H2数据库（目前视频数据来源已完全替换为更完美的API直接获取，数据库作为备用方案）|
| `Apifox后置操作数据库代码.png` | 截图了具体操作的数据库代码|


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
│   │   ├── 📄 demo.trace.db
│   │   ├── 📄 demo_db.mv.db
│   │   └── 📄 demo_db.trace.db
│   │
│   ├── 📁 src/
│   │   └── 📁 main/
│   │       ├── 📁 java/
│   │       │   └── 📁 com/
│   │       │       └── 📁 example/
│   │       │           └── 📁 demo/
│   │       │               ├── 📁 command/

│   │       │               ├── 📁 config/
│   │       │               │   ├── 📄 CorsConfig.java
│   │       │               │   ├── 📄 DataMigrationRunner.java
│   │       │               │   ├── 📄 RestTemplateConfig.java
│   │       │               │   └── 📄 ShellConfig.java
│   │       │               │
│   │       │               ├── 📁 controller/
│   │       │               │   ├── 📄 BannerController.java
│   │       │               │   ├── 📄 BilibiliApiController.java
│   │       │               │   ├── 📄 ImageProxyController.java
│   │       │               │   ├── 📄 SearchController.java
│   │       │               │   ├── 📄 UserController.java
│   │       │               │   ├── 📄 VideoController.java
│   │       │               │   └── 📄 WelcomeController.java
│   │       │               │
│   │       │               ├── 📁 dao/
│   │       │               │   ├── 📄 BannerMessageRepository.java
│   │       │               │   ├── 📄 BilibiliVideoRepository.java
│   │       │               │   ├── 📄 PlaceholderDao.java
│   │       │               │   └── 📄 UserRepository.java
│   │       │               │
│   │       │               ├── 📁 model/
│   │       │               │   ├── 📄 BannerMessage.java
│   │       │               │   ├── 📄 BilibiliVideo.java
│   │       │               │   └── 📄 User.java
│   │       │               │
│   │       │               ├── 📁 service/
│   │       │               │   └── 📄 WelcomeService.java
│   │       │               │
│   │       │               ├── 📁 shell/
│   │       │               │   ├── 📄 DatabaseCommands.java
│   │       │               │   ├── 📄 SystemCommands.java
│   │       │               │   ├── 📄 UserCommands.java
│   │       │               │   └── 📄 VideoCommands.java
│   │       │               │
│   │       │               └── 📄 Application.java
│   │       │
│   │       └── 📁 resources/
│   │           ├── 📁 static/
│   │           │   ├── 🌐 anime.html
│   │           │   ├── 🌐 auth.html
│   │           │   ├── 🌐 esports.html
│   │           │   ├── 🌐 game.html
│   │           │   ├── 🌐 index.html
│   │           │   ├── 🌐 live.html
│   │           │   ├── 🌐 manga.html
│   │           │   ├── 🌐 more.html
│   │           │   ├── 🌐 play.html
│   │           │   ├── 🌐 profile.html
│   │           │   ├── 🌐 search.html
│   │           │   ├── 🌐 test.html
│   │           │   ├── 🌐 video.html
│   │           │   └── 🌐 vip.html
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
│   │   │   │           │   ├── 📄 DataMigrationRunner.class
│   │   │   │           │   ├── 📄 RestTemplateConfig.class
│   │   │   │           │   └── 📄 ShellConfig.class
│   │   │   │           │
│   │   │   │           ├── 📁 controller/
│   │   │   │           │   ├── 📄 BannerController.class
│   │   │   │           │   ├── 📄 BilibiliApiController.class
│   │   │   │           │   ├── 📄 ImageProxyController.class
│   │   │   │           │   ├── 📄 SearchController.class
│   │   │   │           │   ├── 📄 UserController.class
│   │   │   │           │   ├── 📄 VideoController.class
│   │   │   │           │   └── 📄 WelcomeController.class
│   │   │   │           │
│   │   │   │           ├── 📁 dao/
│   │   │   │           │   ├── 📄 BannerMessageRepository.class
│   │   │   │           │   ├── 📄 BilibiliVideoRepository.class
│   │   │   │           │   ├── 📄 PlaceholderDao.class
│   │   │   │           │   └── 📄 UserRepository.class
│   │   │   │           │
│   │   │   │           ├── 📁 model/
│   │   │   │           │   ├── 📄 BannerMessage.class
│   │   │   │           │   ├── 📄 BilibiliVideo.class
│   │   │   │           │   └── 📄 User.class
│   │   │   │           │
│   │   │   │           ├── 📁 service/
│   │   │   │           │   └── 📄 WelcomeService.class
│   │   │   │           │
│   │   │   │           ├── 📁 shell/
│   │   │   │           │   ├── 📄 DatabaseCommands.class
│   │   │   │           │   ├── 📄 SystemCommands.class
│   │   │   │           │   ├── 📄 UserCommands.class
│   │   │   │           │   └── 📄 VideoCommands.class
│   │   │   │           │
│   │   │   │           └── 📄 Application.class
│   │   │   │
│   │   │   ├── 📁 static/
│   │   │   │   ├── 🌐 anime.html
│   │   │   │   ├── 🌐 auth.html
│   │   │   │   ├── 🌐 esports.html
│   │   │   │   ├── 🌐 game.html
│   │   │   │   ├── 🌐 index.html
│   │   │   │   ├── 🌐 live.html
│   │   │   │   ├── 🌐 manga.html
│   │   │   │   ├── 🌐 more.html
│   │   │   │   ├── 🌐 play.html
│   │   │   │   ├── 🌐 profile.html
│   │   │   │   ├── 🌐 search.html
│   │   │   │   ├── 🌐 test.html
│   │   │   │   ├── 🌐 video.html
│   │   │   │   └── 🌐 vip.html
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
│   │   └── 📄 demo-0.0.1-SNAPSHOT.jar.original
│   │
│   ├── 📄 README.md
│   ├── 📝 test-commands.txt
│   ├── 📄 一键运行.bat
│   ├── 📄 spring-shell.log
│   └── 📄 pom.xml
│
├── 📁 frontend/
│   ├── 🌐 anime.html
│   ├── 🌐 auth.html
│   ├── 🌐 esports.html
│   ├── 🌐 game.html
│   ├── 🌐 index.html
│   ├── 🌐 live.html
│   ├── 🌐 manga.html
│   ├── 🌐 more.html
│   ├── 🌐 play.html
│   ├── 🌐 profile.html
│   ├── 🌐 search.html
│   ├── 🌐 test.html
│   ├── 🌐 video.html
│   ├── 🌐 vip.html
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
├── 🖼️ Apifox后置操作数据库代码.png
├── 🐍 generate_directory_tree.py
├── 🐍 Get_video_information_hot.py
├── 🐍 Get_video_information_ranking.py
├── 🐍 migrate_bilibili_to_schema.py
├── 📄 导出数据库：bilibili_videos.sql
├── 📄 导出数据库：data.sql
└── 📄 导出数据库：schema.sql
