@echo off
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
pause