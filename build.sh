#!/usr/bin/env bash

# Developer helper

mvn -q clean package && \
docker build --rm=false --build-arg INDRA_VERSION=1.1.0-SNAPSHOT -t lambdacube/indra:1.1.0-rc1 .