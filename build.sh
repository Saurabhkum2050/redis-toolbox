#!/bin/bash

VERSION=v2
echo "Compiling code & generating jar"
sbt clean compile assembly

echo "Building docker image"
docker build -t redis-toolbox:$VERSION .
docker tag redis-toolbox:$VERSION saurabhkum2050/redis-toolbox:$VERSION
docker push saurabhkum2050/redis-toolbox:$VERSION

#echo "Running docker image"
#docker run --rm -it -e SOURCE_HOST=a33a2da39a7fe4ef5b41425523376760-410013839.us-east-1.elb.amazonaws.com -e SOURCE_PORT=6379 -e SOURCE_SECRET=azYZVR5nCk83xn4gGcAR -e SOURCE_DB=0 -e TARGET_HOST=192.168.0.222 -e TARGET_PORT=6379 -e TARGET_SECRET=shanky -e TARGET_DB=1 --name RedisToolBox saurabhkum2050/redis-toolbox:v1
