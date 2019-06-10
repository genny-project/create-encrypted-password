#!/bin/bash
if [ -z "${1}" ]; then
   version="latest"
else
   version="${1}"
fi

docker push gennyproject/create-encrypted-password:"${version}"
docker tag gennyproject/create-encrypted-password:"${version}" gennyproject/create-encrypted-password:latest
docker push gennyproject/create-encrypted-password:latest
