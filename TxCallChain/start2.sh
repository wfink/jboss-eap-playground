#!/bin/bash

#- start server 2
./bin/standalone.sh -Djboss.server.base.dir=server2 -Djboss.socket.binding.port-offset=100 -Djboss.node.name=server2
