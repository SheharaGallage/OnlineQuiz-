@echo off
echo ========================================
echo   Quiz System - Compile Backend
echo ========================================
echo.

cd backend

echo [1/2] Compiling Java files...
javac server/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Compilation completed successfully!
    echo.
    echo Compiled files:
    dir /B server\*.class
) else (
    echo.
    echo [ERROR] Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Ready to run! Execute 'run-server.bat'
echo ========================================
pause
