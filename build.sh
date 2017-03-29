#!/usr/bin/env bash

# Developer helper

mvn -q clean package && \
docker build --rm=false --build-arg INDRA_VERSION=2.0.1-rc -t lambdacube/indra:2.0.1-rc .
