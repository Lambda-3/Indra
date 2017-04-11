#!/usr/bin/env bash

# Developer helper

mvn -q clean package && \
docker build --rm=false --build-arg INDRA_VERSION=2.0.0-rc4 -t lambdacube/indra:2.0.0-rc4 .
