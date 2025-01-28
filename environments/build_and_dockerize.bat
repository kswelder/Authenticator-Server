@echo off
REM Exit on any error
setlocal enabledelayedexpansion
set ERRLEVEL=0

REM Define the base directory as the parent of the current script's location
cd /d %~dp0..
set BASE_DIR=%cd%

REM Navigate to the project directory
echo Navigating to project directory: %BASE_DIR%
cd /d %BASE_DIR%

REM Build the Docker image
echo Building the Docker image: %DOCKER_IMAGE_NAME%:%DOCKER_TAG%
call docker build -t authentication-server-api:latest -f ./environments/Dockerfile .
IF %ERRORLEVEL% NEQ 0 (
    echo Docker build failed. Exiting...
    exit /b 1
)

REM Success message
echo Docker image [ authentication-server-api:latest ] created successfully!
pause
