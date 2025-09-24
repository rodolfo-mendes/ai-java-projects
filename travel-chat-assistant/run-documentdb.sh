#!/bin/bash

docker run \
    --detach \
    --tty \
    --publish 10260:10260 \
    --env USERNAME=chatdb \
    --env PASSWORD=chat1234 \
    ghcr.io/microsoft/documentdb/documentdb-local:latest
