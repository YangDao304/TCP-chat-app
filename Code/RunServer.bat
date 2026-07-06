@echo off
title TCP Chat Server

cd /d "%~dp0"

echo ===============================
echo Compiling Project...
echo ===============================

javac -encoding UTF-8 gui\*.java client\*.java server\*.java util\*.java

if %errorlevel% neq 0 (
    echo.
    echo Compile Failed!
    pause
    exit
)

echo.
echo Starting Server...
echo.

java -Dfile.encoding=UTF-8 server.Server

pause