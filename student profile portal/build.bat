@echo off
REM ====================================================================
REM  Build & Packaging Script for Student Profile Portal
REM  U21CS504 Web Technology Lab - Assignment 3
REM ====================================================================

set "PROJECT_DIR=%~dp0"
set "TOMCAT_LIB=C:\Users\amirt\Downloads\apache-tomcat-10.1.61-windows-x64\apache-tomcat-10.1.61\lib"
set "SERVLET_JAR=%TOMCAT_LIB%\servlet-api.jar"
set "MYSQL_JAR=%PROJECT_DIR%WebContent\WEB-INF\lib\mysql-connector-j-8.3.0.jar"
set "CLASSES_DIR=%PROJECT_DIR%WebContent\WEB-INF\classes"
set "WAR_FILE=%PROJECT_DIR%StudentPortal.war"
set "TOMCAT_WEBAPPS=C:\Users\amirt\Downloads\apache-tomcat-10.1.61-windows-x64\apache-tomcat-10.1.61\webapps"

echo [1/3] Creating classes output directory...
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"

echo [2/3] Compiling Java source files...
javac -cp "%SERVLET_JAR%;%MYSQL_JAR%" -d "%CLASSES_DIR%" ^
    "%PROJECT_DIR%src\com\student\model\*.java" ^
    "%PROJECT_DIR%src\com\student\util\*.java" ^
    "%PROJECT_DIR%src\com\student\dao\*.java" ^
    "%PROJECT_DIR%src\com\student\servlet\*.java" ^
    "%PROJECT_DIR%src\com\student\filter\*.java"

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)
echo [SUCCESS] Compilation successful!

echo [3/3] Packaging into StudentPortal.war...
cd /d "%PROJECT_DIR%WebContent"
"C:\Program Files\Java\jdk-21.0.12\bin\jar.exe" -cvf "%WAR_FILE%" .

if exist "%TOMCAT_WEBAPPS%" (
    echo Copying WAR to Tomcat webapps...
    copy /y "%WAR_FILE%" "%TOMCAT_WEBAPPS%\StudentPortal.war"
    echo Deploying exploded folder to Tomcat...
    xcopy /y /e /i "%PROJECT_DIR%WebContent" "%TOMCAT_WEBAPPS%\StudentPortal"
    echo [SUCCESS] Application successfully deployed to Tomcat!
)

echo ====================================================================
echo Build Complete!
echo Run Tomcat startup.bat and open http://localhost:8080/StudentPortal/
echo ====================================================================
pause
