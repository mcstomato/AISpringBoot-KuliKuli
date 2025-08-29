# AI SpringBoot 前后端分离项目启动脚本
# 使用方法: .\start-project.ps1

Write-Host "🚀 启动 AI SpringBoot 前后端分离项目" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green

# 检查Maven路径
$mavenPath = "D:\apache-maven-3.9.9\bin\mvn.cmd"
if (-not (Test-Path $mavenPath)) {
    Write-Host "❌ 错误: 找不到Maven，请检查路径: $mavenPath" -ForegroundColor Red
    exit 1
}

# 检查Java环境
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✅ Java环境检查通过: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ 错误: 请确保已安装Java并配置环境变量" -ForegroundColor Red
    exit 1
}

# 启动后端服务
Write-Host "`n🔧 正在启动后端服务..." -ForegroundColor Yellow
Write-Host "后端将运行在: http://localhost:8080" -ForegroundColor Cyan

# 在新窗口中启动后端
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD\backend'; Write-Host '启动后端服务...' -ForegroundColor Green; & '$mavenPath' spring-boot:run"

# 等待后端启动
Write-Host "⏳ 等待后端服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# 启动前端服务
Write-Host "`n🌐 正在启动前端服务..." -ForegroundColor Yellow
Write-Host "前端将运行在: http://localhost:3000" -ForegroundColor Cyan

# 在新窗口中启动前端
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD\frontend'; Write-Host '启动前端服务...' -ForegroundColor Green; python -m http.server 3000"

Write-Host "`n✅ 项目启动完成！" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host "📱 前端地址: http://localhost:3000" -ForegroundColor Cyan
Write-Host "🔧 后端API: http://localhost:8080" -ForegroundColor Cyan
Write-Host "🗄️ 数据库控制台: http://localhost:8080/h2-console" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Green
Write-Host "💡 提示: 按 Ctrl+C 可以停止当前脚本" -ForegroundColor Yellow

# 等待用户输入
Read-Host "`n按回车键退出"
