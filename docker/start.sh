#!/usr/bin/env bash

echo 'started start.sh'
envsubst  < ./config/docker.template.yml > ./config/application.yml

exec java -jar app.jar
