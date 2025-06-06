#!/bin/sh

ACTION=${1:-up}

if [ "$ACTION" = "down" ]; then
  echo 'down'
  docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml down catalog-service order-service
else
#./mvnw -pl catalog-service/ spring-boot:build-image -DskipTests
  echo 'up'
  docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml up -d
fi
exit 0
