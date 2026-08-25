@echo off
setlocal
cd /d "%~dp0"

echo JavaFX uygulamasi Maven ile calistiriliyor...
echo.

if exist "mvnw.cmd" (
    call mvnw.cmd clean javafx:run
) else (
    where mvn >nul 2>nul
    if errorlevel 1 (
        echo Maven bulunamadi: ne 'mvnw.cmd' ne de 'mvn' komutu mevcut.
        echo.
        echo Cozumler:
        echo 1^) Bu proje klasorunde 'mvnw.cmd' dosyasini kullan
        echo 2^) Apache Maven kur ve PATH'e ekle
        echo 3^) IntelliJ icindeki Maven penceresinden 'javafx:run' calistir
        echo.
        pause
        exit /b 1
    )

    mvn clean javafx:run
)

if errorlevel 1 (
    echo.
    echo Hata: Maven veya JavaFX ayarlari kontrol edilmeli.
    pause
    exit /b 1
)

pause



