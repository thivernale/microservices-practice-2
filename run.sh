#!/bin/sh

ACTION=${1:-up}

if [ "$ACTION" = "down" ]; then
  echo 'down'
  docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml -f deployment/docker-compose/monitoring.yml down \
  #notification-service api-gateway catalog-service order-service
else
  echo 'up'
  #./mvnw -pl catalog-service/ spring-boot:build-image -DskipTests
  docker compose -f deployment/docker-compose/infra.yml -f deployment/docker-compose/apps.yml -f deployment/docker-compose/monitoring.yml up -d
fi
exit 0
