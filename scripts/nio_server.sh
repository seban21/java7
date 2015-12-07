#!/bin/sh

WORK_SPACE=./..
JAR=${WORK_SPACE}/lib/*.jar
CP=.:${WORK_SPACE}/bin

for FN in ${JAR}
do
	CP=${CP}:${FN}
done

CP=${CP}

JOPTS="-server -Xmx512m -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:GCPauseIntervalMillis=200"
JOPTS="${JOPTS} -Dfile.encoding=utf-8"

java -version
java ${JOPTS} -cp ${CP} com.example.nio.server.Main 127.0.0.1 8080 8

