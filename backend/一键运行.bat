@echo off

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
echo.
timeout /t 1 /nobreak >nul
start /B cmd /c "timeout /t 5 /nobreak >nul && start http://localhost:8080 && start http://localhost:8080/h2-console"

setlocal enabledelayedexpansion

set "JAR_NAME=demo-0.0.1-SNAPSHOT.jar"
set "JAR_PATH="

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
        goto RUN_JAR
    )
)

:: �ڸ�Ŀ¼������
set "CURRENT_DIR=%cd%"
:PARENT_SEARCH
cd ..
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo �ڸ�Ŀ¼�ҵ�: !JAR_PATH!
    goto RUN_JAR
)

:: ����Ƿ񵽴��Ŀ¼
if not "%cd%"=="%CURRENT_DIR:~0,3%" goto PARENT_SEARCH

:: ���û�ҵ��ļ�
echo ����: δ�ҵ� %JAR_NAME%
pause
exit /b 1

:RUN_JAR
echo ��������: java -jar "!JAR_PATH!"
java -jar "!JAR_PATH!"
