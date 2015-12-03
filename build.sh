#!/bin/sh

git_url=git@git.oschina.net:lisanlai/yijizhang.git
echo "Start pull code from $git_url"

#拉取代码
git pull

echo "Pull code finished!"

#编译-打包
echo "Start compile and package project."
mvn clean package -P production
echo "Package project finished!"

#创建文件夹
d="../RunningWar/"
if [ ! -d $d ]; then
  mkdir -p $d
fi

#拷贝war包
war=`find ./target -name '*-exec.war'`
if [ ! -n $war ] || [ "$pid"!="" ]; then
    w=${war##*/}
    echo "Copy $war to $d"
    cp -f $war $d
    echo "chmod 755 $d$w"
    chmod 755 $d$w
fi

#关闭旧进程
echo "Check if pid exist......"
pid=`ps -ef |grep java |awk '{print $2}'`
if [ ! -n $pid ] || [ "$pid"!="" ]; then
    echo "Project is running ...pid=$pid"
    echo "Kill -9 $pid"
    kill -9 $pid
else
    echo "Project is not running."
fi

#运行war包
echo "Start run project....."
java -jar $d$w &

