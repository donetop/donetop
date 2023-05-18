#!/bin/bash

RUNNING=0
check_running_java_process() {
	PROCESS_NAME="$1"
	SEARCHED_PROCESS_ID=$(pgrep -l -f -o "$PROCESS_NAME" | cut -d' ' -f1)
	SEARCHED_PROCESS_NAME=$(pgrep -l -f -o "$PROCESS_NAME" | cut -d' ' -f2)
	if [ "$SEARCHED_PROCESS_NAME" == "java" ]; then
      RUNNING=1
	else
			RUNNING=0
  fi
}

start() {
		# Start process.
		APPLICATION_NAME="$1"
		check_running_java_process "$APPLICATION_NAME"
		if [ "$RUNNING" == 1 ]; then
				echo "There is already running $APPLICATION_NAME process($SEARCHED_PROCESS_ID)."
		else
				SRC_LIB_DIR="/donetop/$APPLICATION_NAME/libs"
      	JAR_FULL_PATH=$(find "$SRC_LIB_DIR" -name "*.jar")
        echo "starting $APPLICATION_NAME process."
        java -server -Xms128m -Xmx128m -Dspring.profiles.active=aws -jar "$JAR_FULL_PATH" > "/donetop/$APPLICATION_NAME/logs/start.log" &
		fi
}

stop() {
    # Stop process.
    APPLICATION_NAME="$1"
    check_running_java_process "$APPLICATION_NAME"
		if [ "$RUNNING" == 0 ]; then
				echo "There is no running $APPLICATION_NAME process."
		else
			  echo "shutting down $APPLICATION_NAME($SEARCHED_PROCESS_ID) process."
        pkill -9 -e -f -o "$APPLICATION_NAME"
		fi
}

## See how we call this script.
case "$1" in
  	start)
        if [ -z "$2" ]; then
          	echo "application-name should be supplied."
        else
          	start "$2"
        fi
        ;;
  	stop)
        if [ -z "$2" ]; then
          	echo "application-name should be supplied."
        else
          	stop "$2"
        fi
        ;;
		restart)
				if [ -z "$2" ]; then
						echo "application-name should be supplied."
				else
						stop "$2"
						start "$2"
				fi
				;;
  	*)
        echo $"Usage: $0 <start|stop|restart> <application-name>"
        exit 1
esac
