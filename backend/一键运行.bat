@echo off

<<<<<<< HEAD
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
=======
chcp 936 >nul

echo.
echo  �X�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�[
echo  �U           STOMATO            �U
echo  �d�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�g
echo  �U ��������: 2581775404@qq.com  �U
echo  �U ѧ��: 20222281               �U
echo  �d�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�g
echo  �U      �����뷴������          �U
echo  �^�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�a
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
echo.
timeout /t 1 /nobreak >nul
start /B cmd /c "timeout /t 5 /nobreak >nul && start http://localhost:8080 && start http://localhost:8080/h2-console"

setlocal enabledelayedexpansion

set "JAR_NAME=demo-0.0.1-SNAPSHOT.jar"
set "JAR_PATH="

<<<<<<< HEAD
echo 正在搜索 %JAR_NAME% ...

:: 在当前目录搜索
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo 在当前目录找到: !JAR_PATH!
    goto RUN_JAR
)

:: 在子目录中搜索
for /r . %%i in (%JAR_NAME%) do (
    if exist "%%i" (
        set "JAR_PATH=%%i"
        echo 在子目录找到: !JAR_PATH!
=======
echo �������� %JAR_NAME% ...

:: �ڵ�ǰĿ¼����
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo �ڵ�ǰĿ¼�ҵ�: !JAR_PATH!
    goto RUN_JAR
)

:: ����Ŀ¼������
for /r . %%i in (%JAR_NAME%) do (
    if exist "%%i" (
        set "JAR_PATH=%%i"
        echo ����Ŀ¼�ҵ�: !JAR_PATH!
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
        goto RUN_JAR
    )
)

<<<<<<< HEAD
:: 在父目录中搜索
=======
:: �ڸ�Ŀ¼������
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
set "CURRENT_DIR=%cd%"
:PARENT_SEARCH
cd ..
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
<<<<<<< HEAD
    echo 在父目录找到: !JAR_PATH!
    goto RUN_JAR
)

:: 检查是否到达根目录
if not "%cd%"=="%CURRENT_DIR:~0,3%" goto PARENT_SEARCH

:: 如果没找到文件
echo 错误: 未找到 %JAR_NAME%
=======
    echo �ڸ�Ŀ¼�ҵ�: !JAR_PATH!
    goto RUN_JAR
)

:: ����Ƿ񵽴��Ŀ¼
if not "%cd%"=="%CURRENT_DIR:~0,3%" goto PARENT_SEARCH

:: ���û�ҵ��ļ�
echo ����: δ�ҵ� %JAR_NAME%
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
pause
exit /b 1

:RUN_JAR
<<<<<<< HEAD
echo 正在运行: java -jar "!JAR_PATH!"
=======
echo ��������: java -jar "!JAR_PATH!"
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
java -jar "!JAR_PATH!"
