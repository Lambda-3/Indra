#!/usr/bin/env bash

# Developer helper

mvn clean package && \
docker build --rm=false --build-arg INDRA_VERSION=1.0.0-SNAPSHOT -t indra:1.0-rc .