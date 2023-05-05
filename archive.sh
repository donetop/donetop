#!/bin/bash

archive_jar() {

  	APPLICATION_NAME="$1"
  	SRC_LIB_DIR="$APPLICATION_NAME/build/libs"
  	DST_LIB_DIR="/donetop/$APPLICATION_NAME/libs"

  	JAR_FULL_PATH=$(find "$SRC_LIB_DIR" -name "*.jar")
  	JAR_NAME_WITH_EXTENSION=$(basename "$JAR_FULL_PATH")
  	JAR_NAME_WITHOUT_EXTENSION="${JAR_NAME_WITH_EXTENSION%.*}"

  	CURRENT_DATETIME=$(date '+%Y-%m-%dT%H-%M-%S')
  	JAR_NAME_WITH_DATETIME="$JAR_NAME_WITHOUT_EXTENSION-$CURRENT_DATETIME.jar"

  	echo "copy $JAR_NAME_WITH_EXTENSION name with $JAR_NAME_WITH_DATETIME"
  	cp "$SRC_LIB_DIR/$JAR_NAME_WITH_EXTENSION" "$SRC_LIB_DIR/$JAR_NAME_WITH_DATETIME"

  	echo "move $JAR_NAME_WITH_DATETIME from $SRC_LIB_DIR to $DST_LIB_DIR"
  	mv "$SRC_LIB_DIR/$JAR_NAME_WITH_DATETIME" "$DST_LIB_DIR/$JAR_NAME_WITH_DATETIME"

}

if [[ $# -ne 1 ]]; then
		echo $"Usage: $0 <application-name>"
  	exit 1
else
		archive_jar "$1"
fi
