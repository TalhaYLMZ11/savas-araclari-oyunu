@echo off
setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
if "%SCRIPT_DIR:~-1%"=="\" set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

set "MVN_VERSION=3.9.9"
set "MVN_FOLDER=apache-maven-%MVN_VERSION%"
set "MVN_URL=https://archive.apache.org/dist/maven/maven-3/%MVN_VERSION%/binaries/%MVN_FOLDER%-bin.zip"

if defined LOCALAPPDATA (
    set "CACHE_ROOT=%LOCALAPPDATA%\SavasAraclariMaven"
) else (
    set "CACHE_ROOT=%USERPROFILE%\.cache\SavasAraclariMaven"
)

set "INSTALL_DIR=%CACHE_ROOT%\%MVN_FOLDER%"
set "MAVEN_CMD=%INSTALL_DIR%\bin\mvn.cmd"
set "ZIP_FILE=%CACHE_ROOT%\%MVN_FOLDER%-bin.zip"

if not exist "%MAVEN_CMD%" (
    echo Maven bulunamadi, indiriliyor...
    if not exist "%CACHE_ROOT%" mkdir "%CACHE_ROOT%"

    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "$ProgressPreference='SilentlyContinue';" ^
        "Invoke-WebRequest -Uri '%MVN_URL%' -OutFile '%ZIP_FILE%'"
    if errorlevel 1 (
        echo Maven indirilemedi.
        exit /b 1
    )

    if exist "%INSTALL_DIR%" rmdir /s /q "%INSTALL_DIR%"
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "Expand-Archive -Path '%ZIP_FILE%' -DestinationPath '%CACHE_ROOT%' -Force"
    if errorlevel 1 (
        echo Maven acilamadi.
        exit /b 1
    )

    del /q "%ZIP_FILE%" >nul 2>nul
)

if not exist "%MAVEN_CMD%" (
    echo Maven calistirma dosyasi bulunamadi: "%MAVEN_CMD%"
    exit /b 1
)

"%MAVEN_CMD%" %*
exit /b %ERRORLEVEL%


