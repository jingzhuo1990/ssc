#!/bin/sh
java -jar ${JAVA_OPTS} /root/deploy/ssc-1.0-SNAPSHOT.jar
tail -f /dev/null