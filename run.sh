#!/bin/sh

docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml up -d

#./mvnw -pl catalog-service/ spring-boot:build-image -DskipTests
# docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml down catalog-service
