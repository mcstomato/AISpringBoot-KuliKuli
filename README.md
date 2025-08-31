# 🔗 该项目已成功部署至服务器
# 🔗 主页地址：60.205.198.37:80
# 🔗 什么？ 你问我一个贫困大学生服务器是哪来的？

<details>
<summary>点击查看被隐藏的内容</summary>

这里是被隐藏的文本内容，只有在点击上方标题后才会显示出来。

```javascript
// 甚至可以隐藏代码示例
function example() {
  console.log("这是被隐藏的代码");
}
```

</details>


## ⚡ 放在最前面的提示

后端的详细使用说明在backend文件

若要进行运行请务必认真观看

运行项目.bat是需要整个项目文件才可以运行

一键启动.bat仅需要demo-0.0.1-SNAPSHOT.jar即可运行

一键启动会自动搜索附近的jar文件，不过还是最好放在同一级目录下

仅仅查看作业的效果强烈建议使用jar文件+一键启动！！！

因为这几天实习我基本都在高强度码代码(戳啦 其实是ai码代码)

搞得我已经忘了该项目哪些地方用了绝对路径....

老师你的仓库貌似满了，可能需要清理一下

虽然我东西看起来有亿点多，但是只有4MB左右

所以补药清理我的球球乐

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
| `运行项目.bat`     | 运行SpringBoot服务端(开发模式),需要编辑Maven地址| 
| `mysql导出bilibili_videos.sql.bat` | 用于将mysql数据库进行导出sql，并将mysql的sql文件信息移植进H2数据库中|
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
