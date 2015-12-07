#!/bin/sh

WORK_SPACE=./..
JAR=${WORK_SPACE}/lib/*.jar
CP=.:${WORK_SPACE}/bin

for FN in ${JAR}
do
	CP=${CP}:${FN}
done

CP=${CP}

JOPTS="-server -Xmx512m -Xms512m -XX:MaxPermSize=128m"
JOPTS="${JOPTS} -Dfile.encoding=utf-8"

java -version
java ${JOPTS} -cp ${CP} com.example.test.io.client.Main 100 100000

