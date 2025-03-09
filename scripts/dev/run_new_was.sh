#!/bin/bash

REPOSITORY=/home/ubuntu/app
SCOUTER=/home/ubuntu/scouter

TARGET_PORT=8080

echo "> Kill WAS running at ${TARGET_PORT}."
sudo kill ${TARGET_PID}

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME에 실행권한 추가"
chmod +x $JAR_NAME

LOG_FILE="$REPOSITORY/nohup.out"

echo "> 로그 파일이 존재하지 않으면 생성하고, 존재하면 내용을 유지 (쓰기 권한 확인)"
touch $LOG_FILE

echo "> $JAR_NAME 실행"
nohup java -jar \
          -Dserver.port=${TARGET_PORT} \
          -Dspring.profiles.active=dev \
          -Ddb.endpoint=${DB_ENDPOINT} \
          -Ddb.username=${DB_USERNAME} \
          -Ddb.password=${DB_PASSWORD} \
          -Djwt.secret-key=${JWT_SECRET_KEY} \
          $JAR_NAME > $LOG_FILE 2>&1 &

echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0
