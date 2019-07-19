#!/bin/bash

# configure the server without any additional elytron configuration (which worked for EAP until 7.1.x)

if [ -z "$1" -o ! -d "$1" -o ! -d "$1/standalone" ];then
  echo "usage $0 EAP/WildFly HOME"
  exit 1
fi

jbossHome=$1

#- copy standalone directory into server1 and server2, optional server3

echo "Creating server1, server2, server3"
cp -R $jbossHome/standalone/ $jbossHome/server1
cp -R $jbossHome/standalone/ $jbossHome/server2
cp -R $jbossHome/standalone/ $jbossHome/server3

echo "copy startup scripts"
cp start1.sh start2.sh start3.sh $jbossHome

#- add an application user to the server
echo "add users"
$jbossHome/bin/add-user.sh -sc $jbossHome/server1/configuration -a -u client -p client1
# server@server1 user needed for loopback
$jbossHome/bin/add-user.sh -sc $jbossHome/server1/configuration -a -u server -p server1
$jbossHome/bin/add-user.sh -sc $jbossHome/server2/configuration -a -u server -p server1
$jbossHome/bin/add-user.sh -sc $jbossHome/server3/configuration -a -u server -p server1

# copy the application files
echo "deploy applications"
cp appOne/ear/target/TxCallChain-AppOne.ear $jbossHome/server1/deployments
cp appTwo/ear/target/TxCallChain-AppTwo.ear $jbossHome/server2/deployments
cp appThree/ear/target/TxCallChain-AppThree.ear $jbossHome/server3/deployments


echo "configure servers"
$jbossHome/bin/jboss-cli.sh -Djboss.server.base.dir=server1 --file=server1.cli
$jbossHome/bin/jboss-cli.sh -Djboss.server.base.dir=server2 --file=server2.cli
$jbossHome/bin/jboss-cli.sh -Djboss.server.base.dir=server3 --file=server3.cli
rm -fr server[123]
