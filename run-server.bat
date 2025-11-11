@echo off
echo ========================================
echo   Quiz System - Starting Server
echo ========================================
echo.

cd backend

echo Starting Quiz Server...
echo Server will listen on port 8080
echo UDP Timer will use port 9999
echo.
echo Press Ctrl+C to stop the server
echo ========================================
echo.

java -cp . server.QuizServer

pause
