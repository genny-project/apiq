#!/bin/bash

./build-native.sh
#./mvnw package -Pnative -Dquarkus.native.container-build=true -DskipTests=true
docker build -f src/main/docker/Dockerfile.native -t gennyproject/apiq:latest .
docker tag gennyproject/apiq:latest gennyproject/apiq:8.0.0

