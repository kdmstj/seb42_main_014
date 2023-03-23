#!/bin/bash
BUILD_JAR=$(ls /home/ssm-user/action/build/libs/volunteer-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)

sudo chmod 777 /home/ssm-user/action/

echo "> 현재 시간: $(date)" >> /home/ssm-user/action/deploy.log

echo "> build 파일명: $JAR_NAME" >> /home/ssm-user/action/deploy.log

echo "> build 파일 복사" >> /home/ssm-user/action/deploy.log
DEPLOY_PATH=/home/ssm-user/action/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ssm-user/action/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ssm-user/action/deploy.log
else
  echo "> kill -9 $CURRENT_PID" >> /home/ssm-user/action/deploy.log
  sudo kill -9 $CURRENT_PID
  sleep 5
fi


source ~/.bashrc

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ssm-user/action/deploy.log
sudo nohup java -jar $DEPLOY_JAR >> /home/ssm-user/deploy.log 2>/home/ssm-user/action/deploy_err.log &
