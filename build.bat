@echo off
setlocal enabledelayedexpansion

echo.
echo  =========================================================
echo   Microfinance ^& Loan Management System - Build Script
echo  =========================================================
echo.

:: ── Config ────────────────────────────────────────────────────────────────────
set SRC_DIR=src\main\java
set OUT_DIR=out
set LIB_DIR=lib
set JAR_NAME=mysql-connector-j-8.0.33.jar
set JAR_URL=https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
set MAIN_CLASS=com.microfinance.Main

:: ── Step 1: Create output directories ────────────────────────────────────────
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

:: ── Step 2: Download JDBC driver if not present ───────────────────────────────
if not exist "%LIB_DIR%\%JAR_NAME%" (
    echo  [1/3] Downloading MySQL JDBC Driver...
    powershell -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%LIB_DIR%\%JAR_NAME%' -UseBasicParsing"
    if errorlevel 1 (
        echo.
        echo  ERROR: Failed to download MySQL JDBC driver.
        echo  Please check your internet connection, then try again.
        echo.
        pause
        exit /b 1
    )
    echo  [1/3] JDBC Driver downloaded successfully.
) else (
    echo  [1/3] JDBC Driver already present. Skipping download.
)

:: ── Step 3: Collect all .java source files ────────────────────────────────────
echo  [2/3] Compiling sources...
dir /s /b "%SRC_DIR%\*.java" > sources.txt 2>nul

javac -encoding UTF-8 -cp "%LIB_DIR%\%JAR_NAME%" -d "%OUT_DIR%" @sources.txt 2>&1
if errorlevel 1 (
    echo.
    echo  ERROR: Compilation failed. See errors above.
    del sources.txt
    pause
    exit /b 1
)
del sources.txt
echo  [2/3] Compilation successful.

:: ── Step 4: Run the application ───────────────────────────────────────────────
echo  [3/3] Starting application...
echo.
java -cp "%OUT_DIR%;%LIB_DIR%\%JAR_NAME%" -Dfile.encoding=UTF-8 %MAIN_CLASS%

echo.
echo  Application exited.
pause
