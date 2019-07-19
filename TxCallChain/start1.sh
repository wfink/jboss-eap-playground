#!/bin/bash

#- start server 1
./bin/standalone.sh -Djboss.server.base.dir=server1 -Djboss.socket.binding.port-offset=0 -Djboss.node.name=server1
