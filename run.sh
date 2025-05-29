#!/bin/sh

docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml up -d
