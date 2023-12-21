PROJECT_NAME=demo # 프로젝트 이름 설정
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}-.*.jar | head -n 1) # 현재 실행 중인 프로세스 중에서 프로젝트 이름에 해당하는 Jar 파일을 찾아 해당 PID를 가져온다.

if [ -z "$CURRENT_PID" ]; then
    echo " 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo " 구동중인 애플리케이션을 종료했습니다. (pid : $CURRENT_PID)"
    kill -15 $CURRENT_PID
fi

JAR_NAME=$(ls | grep .jar | head -n 1)

echo "\n SpringBoot 애플리케이션 $JAR_NAME을 실행합니다.\n"

sudo -E nohup java -Xmx2048m -jar $JAR_NAME & # 해당 JAR 파일을 백그라운드에서 실행한다.
# -E는 환경 변수를 유지하고 nohup은 로그아웃 후에도 프로세스가 계속 실행되도록 한다.
