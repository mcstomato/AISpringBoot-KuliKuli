@echo off
chcp 65001 >nul

echo.
echo ====================================
echo           STOMATO
echo ====================================
echo 作者邮箱: 2581775404@qq.com
echo 学号: 20222281
echo ====================================
echo      问题请反馈邮箱
echo ====================================
echo.

REM 延迟5秒后打开浏览器
start /B "" cmd /c "timeout /t 5 /nobreak >nul && start "" http://localhost:8080"
start /B "" cmd /c "timeout /t 5 /nobreak >nul && start "" http://localhost:8080/h2-console"

set "JAR_NAME=demo-0.0.1-SNAPSHOT.jar"
set "JAR_PATH="

echo 正在搜索 %JAR_NAME% ...

REM 在当前目录搜索
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo 在当前目录找到: %JAR_PATH%
    goto RUN_JAR
)

REM 在子目录中搜索
for /f "delims=" %%i in ('dir /s /b "%JAR_NAME%" 2^>nul') do (
    set "JAR_PATH=%%i"
    echo 在子目录找到: %%i
    goto RUN_JAR
)

REM 在父目录中搜索
set "PARENT_DIR=%~dp0.."
cd /d "%PARENT_DIR%"
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo 在父目录找到: %JAR_PATH%
    goto RUN_JAR
)

echo 错误: 未找到 %JAR_NAME%
pause
exit /b 1

:RUN_JAR
echo 正在运行: java -jar "%JAR_PATH%"
java -jar "%JAR_PATH%"
pause