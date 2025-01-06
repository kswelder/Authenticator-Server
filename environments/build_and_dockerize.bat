@echo off
REM Exit on any error
setlocal enabledelayedexpansion
set ERRLEVEL=0

REM Define the base directory as the parent of the current script's location
cd /d %~dp0..
set BASE_DIR=%cd%

REM Variables
set DOCKER_IMAGE_NAME=authentication-server-api
set DOCKER_TAG=latest

REM Navigate to the project directory
echo Navigating to project directory: %BASE_DIR%
cd /d %BASE_DIR%

REM Build the Spring Boot project using Maven
echo Building the Spring Boot application...
call mvn clean package
IF %ERRORLEVEL% NEQ 0 (
    echo Maven build failed. Exiting...
    exit /b 1
)

echo Now in environments directory: %cd%

REM Check if Dockerfile exists
IF NOT EXIST "Dockerfile" (
    echo Dockerfile not found in %cd%. Exiting...
    exit /b 1
)

REM Build the Docker image
echo Building the Docker image: %DOCKER_IMAGE_NAME%:%DOCKER_TAG%
call docker build -t %DOCKER_IMAGE_NAME%:%DOCKER_TAG% -f ./environments/Dockerfile .
IF %ERRORLEVEL% NEQ 0 (
    echo Docker build failed. Exiting...
    exit /b 1
)

REM Success message
echo Docker image %DOCKER_IMAGE_NAME%:%DOCKER_TAG% created successfully!
pause
