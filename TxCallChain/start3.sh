#!/bin/bash

#- start server 3 optional
./bin/standalone.sh -Djboss.server.base.dir=server3 -Djboss.socket.binding.port-offset=200 -Djboss.node.name=server3
