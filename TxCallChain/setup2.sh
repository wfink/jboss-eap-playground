#!/bin/bash

if [ -z "$1" -o ! -d $1 -o ! -d $1/standalone ];then
  echo "usage $0 EAP/WildFly HOME"
  exit 1
fi

jbossHome=$1

if [ ! -d $jbossHome/server1 ];then
  echo "run setup.sh first"
  exit 1
fi

echo "add the server level user to all servers"
serverUser="serverUser"
serverPass="redhat1!"
serverUserConfig="serverLevelUserConfig.cli"
$jbossHome/bin/add-user.sh -sc $jbossHome/server1/configuration -a -u $serverUser -p $serverPass
$jbossHome/bin/add-user.sh -sc $jbossHome/server2/configuration -a -u $serverUser -p $serverPass
$jbossHome/bin/add-user.sh -sc $jbossHome/server3/configuration -a -u $serverUser -p $serverPass

$jbossHome/bin/jboss-cli.sh -Djboss.server.base.dir=server1 --file=$serverUserConfig
$jbossHome/bin/jboss-cli.sh -Djboss.server.base.dir=server2 --file=$serverUserConfig
$jbossHome/bin/jboss-cli.sh -Djboss.server.base.dir=server3 --file=$serverUserConfig


