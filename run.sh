#!/bin/bash

# Dynamically detect user's home directory and build .env path
ENV_FILE="$HOME/.gradle/.env"

# Check if .env file exists
if [ ! -f "$ENV_FILE" ]; then
  echo "❌ .env file not found at $ENV_FILE"
  exit 1
fi

# Export variables from .env file safely
set -a
# shellcheck disable=SC1090
source "$ENV_FILE"
set +a

# Verify required variables
REQUIRED_VARS=(DB_TYPE DB_HOST DB_PORT DB_NAME DB_USER DB_PASSWORD)
for var in "${REQUIRED_VARS[@]}"; do
  if [ -z "${!var}" ]; then
    echo "❌ Missing required variable: $var"
    exit 1
  fi
done

echo "🧹 Cleaning previous build artifacts..."
./gradlew clean || { echo "❌ Gradle clean failed"; exit 1; }

echo "⚙️ Building WAR file..."
./gradlew :app:build || { echo "❌ Build failed"; exit 1; }

shopt -s nullglob
WAR_FILES=(app/build/libs/*.war)

if [ ${#WAR_FILES[@]} -eq 0 ]; then
  echo "❌ No WAR files found after build."
  exit 1
fi

# Try to start app with the first valid WAR
for WAR_PATH in "${WAR_FILES[@]}"; do
  echo "🚀 Attempting to start Spring Boot application with: $WAR_PATH"
  java \
    -Dspring.datasource.url="jdbc:${DB_TYPE}://${DB_HOST}:${DB_PORT}/${DB_NAME}" \
    -Dspring.datasource.username="$DB_USER" \
    -Dspring.datasource.password="$DB_PASSWORD" \
    ${SPRING_PROFILES_ACTIVE:+-Dspring.profiles.active="$SPRING_PROFILES_ACTIVE"} \
    -jar "$WAR_PATH" && break

  echo "❌ Failed to start with: $WAR_PATH, trying next..."
done
