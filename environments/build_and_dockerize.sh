#!/bin/bash

# Exit script on any error
set -e

# Define the base directory as the parent of the current script's location
BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"

# Variables
DOCKER_IMAGE_NAME="authentication-server-api"   # Replace with your image name
DOCKER_TAG="latest"                             # Replace with your image tag

# Navigate to the project directory
echo "Navigating to project directory: $BASE_DIR"
cd $BASE_DIR

# Build the Spring Boot project using Maven
echo "Building the Spring Boot application..."
call mvn clean package

# Build the Docker image
echo "Building the Docker image: $DOCKER_IMAGE_NAME:$DOCKER_TAG"
call docker build -t $DOCKER_IMAGE_NAME:$DOCKER_TAG -f ./environments/Dockerfile .

# Print success message
echo "Docker image $DOCKER_IMAGE_NAME:$DOCKER_TAG created successfully!"
