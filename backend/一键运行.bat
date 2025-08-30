@echo off

<<<<<<< HEAD
chcp 65001 >nul

echo.
echo ====================================
echo           STOMATO
echo ====================================
echo ä½œè€…é‚®ç®±: 2581775404@qq.com
echo å­¦å·: 20222281
echo ====================================
echo      é—®é¢˜è¯·åé¦ˆé‚®ç®±
echo ====================================
=======
chcp 936 >nul

echo.
echo  ¨X¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨[
echo  ¨U           STOMATO            ¨U
echo  ¨d¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨g
echo  ¨U ×÷ÕßÓÊÏä: 2581775404@qq.com  ¨U
echo  ¨U Ñ§ºÅ: 20222281               ¨U
echo  ¨d¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨g
echo  ¨U      ÎÊÌâÇë·´À¡ÓÊÏä          ¨U
echo  ¨^¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨T¨a
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
echo.
timeout /t 1 /nobreak >nul
start /B cmd /c "timeout /t 5 /nobreak >nul && start http://localhost:8080 && start http://localhost:8080/h2-console"

setlocal enabledelayedexpansion

set "JAR_NAME=demo-0.0.1-SNAPSHOT.jar"
set "JAR_PATH="

<<<<<<< HEAD
echo æ­£åœ¨æœç´¢ %JAR_NAME% ...

:: åœ¨å½“å‰ç›®å½•æœç´¢
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo åœ¨å½“å‰ç›®å½•æ‰¾åˆ°: !JAR_PATH!
    goto RUN_JAR
)

:: åœ¨å­ç›®å½•ä¸­æœç´¢
for /r . %%i in (%JAR_NAME%) do (
    if exist "%%i" (
        set "JAR_PATH=%%i"
        echo åœ¨å­ç›®å½•æ‰¾åˆ°: !JAR_PATH!
=======
echo ÕýÔÚËÑË÷ %JAR_NAME% ...

:: ÔÚµ±Ç°Ä¿Â¼ËÑË÷
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
    echo ÔÚµ±Ç°Ä¿Â¼ÕÒµ½: !JAR_PATH!
    goto RUN_JAR
)

:: ÔÚ×ÓÄ¿Â¼ÖÐËÑË÷
for /r . %%i in (%JAR_NAME%) do (
    if exist "%%i" (
        set "JAR_PATH=%%i"
        echo ÔÚ×ÓÄ¿Â¼ÕÒµ½: !JAR_PATH!
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
        goto RUN_JAR
    )
)

<<<<<<< HEAD
:: åœ¨çˆ¶ç›®å½•ä¸­æœç´¢
=======
:: ÔÚ¸¸Ä¿Â¼ÖÐËÑË÷
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
set "CURRENT_DIR=%cd%"
:PARENT_SEARCH
cd ..
if exist "%JAR_NAME%" (
    set "JAR_PATH=%cd%\%JAR_NAME%"
<<<<<<< HEAD
    echo åœ¨çˆ¶ç›®å½•æ‰¾åˆ°: !JAR_PATH!
    goto RUN_JAR
)

:: æ£€æŸ¥æ˜¯å¦åˆ°è¾¾æ ¹ç›®å½•
if not "%cd%"=="%CURRENT_DIR:~0,3%" goto PARENT_SEARCH

:: å¦‚æžœæ²¡æ‰¾åˆ°æ–‡ä»¶
echo é”™è¯¯: æœªæ‰¾åˆ° %JAR_NAME%
=======
    echo ÔÚ¸¸Ä¿Â¼ÕÒµ½: !JAR_PATH!
    goto RUN_JAR
)

:: ¼ì²éÊÇ·ñµ½´ï¸ùÄ¿Â¼
if not "%cd%"=="%CURRENT_DIR:~0,3%" goto PARENT_SEARCH

:: Èç¹ûÃ»ÕÒµ½ÎÄ¼þ
echo ´íÎó: Î´ÕÒµ½ %JAR_NAME%
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
pause
exit /b 1

:RUN_JAR
<<<<<<< HEAD
echo æ­£åœ¨è¿è¡Œ: java -jar "!JAR_PATH!"
=======
echo ÕýÔÚÔËÐÐ: java -jar "!JAR_PATH!"
>>>>>>> 466ced9560b2a268847c5a07e001441a5509f14f
java -jar "!JAR_PATH!"
