# AI SpringBoot 后端项目

## 项目结构
```
backend/
├── src/
│   └── main/
│       ├── java/com/example/demo/
│       │   ├── Application.java          # 主启动类
│       │   ├── controller/               # 控制器层
│       │   ├── service/                  # 服务层
│       │   ├── dao/                      # 数据访问层
│       │   ├── model/                    # 实体类
│       │   └── config/                   # 配置类
│       └── resources/
│           ├── application.properties    # 应用配置
│           ├── schema.sql               # 数据库结构
│           ├── data.sql                 # 初始数据
│           └── bilibili_videos.sql      # B站视频数据
├── data/                                # 数据库文件
├── pom.xml                              # Maven配置
└── README.md                            # 项目说明
```

## 环境要求
- Java 1.8+
- Maven 3.6+
- H2数据库 (内置)

## 构建和运行

### 使用PowerShell
```powershell
# 进入后端目录
cd backend

# 编译项目
& "D:\apache-maven-3.9.9\bin\mvn.cmd" clean compile

# 运行项目
& "D:\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run
```

### 使用命令行
```bash
# 进入后端目录
cd backend

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

## API接口

### 视频相关接口
- `GET /api/videos` - 获取视频列表
- `GET /api/videos/{id}` - 获取单个视频
- `POST /api/videos` - 添加视频
- `PUT /api/videos/{id}` - 更新视频
- `DELETE /api/videos/{id}` - 删除视频

### Banner相关接口
- `GET /api/banners` - 获取Banner列表
- `POST /api/banners` - 添加Banner
- `PUT /api/banners/{id}` - 更新Banner
- `DELETE /api/banners/{id}` - 删除Banner

### 图片代理接口
- `GET /api/proxy/image` - 图片代理服务

## 数据库
- 类型: H2内存数据库
- 控制台: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:demo`
- 用户名: `sa`
- 密码: (留空)

## 前端集成
- 前端项目位于 `../frontend/` 目录
- 前端默认运行在 http://localhost:3000
- 已配置CORS支持跨域访问

## 注意事项
- 数据存储在内存中，重启后数据会丢失
- 如需持久化存储，可修改为文件数据库或MySQL
- 确保前端服务已启动以正常使用完整功能