#!/bin/sh

export JAVA_HOME=/usr/local/java
export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH


#1,start supervisord
/usr/bin/supervisord &

#2,start tomcat
cd /u01/transport-manage-web/


if [ "${START_ENV}" = "online" ]; then
  java ${JAVA_OPTS} -jar -javaagent:/u01/pinpoint-agent/pinpoint-bootstrap-1.8.4.jar transport-manage-web.jar --spring.profiles.active=${START_ENV} --Drocketmq.client.logRoot=/u01/transport-manage-web/log/mq.log --Drocketmq.client.logLevel=INFO --server.port=8080 >/u01/transport-manage-web/log/startup.log 2>&1
else
  java ${JAVA_OPTS} -jar transport-manage-web.jar --spring.profiles.active=${START_ENV} --Drocketmq.client.logRoot=/u01/transport-manage-web/log/mq.log --Drocketmq.client.logLevel=INFO --server.port=8080 >/u01/transport-manage-web/log/startup.log 2>&1
fi

#3,process
