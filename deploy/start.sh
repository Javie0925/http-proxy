#!/bin/sh
APP_NAME=http-proxy
MAIN_CLASS=priv.jv.httpproxy.EasyHttpProxyServer
APP_HOME=`cd $(dirname $0);pwd`
# PATH=/usr/java/jdk1.8.0_20/bin:$PATH:$HOME/bin:/sbin:/usr/bin:/usr/sbin:$APP_HOME/lib
JAVA_COM=`which java`

CONF_DIR=$APP_HOME/config:$APP_HOME/resources

LIB_DIR=$APP_HOME/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JVM_OPTS="-Xms128m -Xmx1024m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError \
    -Xloggc:$APP_HOME/logs/gc.log -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
    -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=8 -XX:GCLogFileSize=64m"

# AGENT="-javaagent:$APP_HOME/jagent/pinpoint/pinpoint-bootstrap-1.8.4.jar -Dpinpoint.agentId=api-gateway-001 -Dpinpoint.applicationName=api-gateway"

DEGUB='-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'

echo "Java Version:"$JAVA_COM
echo "Starting the server of $APP_NAME"
echo "AGENT:$AGENT"
cd $APP_HOME
echo "Current Dir: "`pwd`
nohup $JAVA_COM $DEBUG $AGENT $JVM_OPTS \
        -Djava.awt.headless=true -Dspring.profiles.active=staging \
                -Dservicename=http-proxy \
        -Dfile.encoding=utf-8 \
        -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS > $APP_HOME/boot.log 2>&1 & echo $!> $APP_HOME/run.pid
sleep 5
echo "Done.."
exit 0;
