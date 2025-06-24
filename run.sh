#!/bin/bash

# ---------------------------

declare dc_infra=deployment/docker-compose/infra.yml
declare dc_app=deployment/docker-compose/apps.yml
declare dc_mon=deployment/docker-compose/monitoring.yml

function build_image() {
  ./mvnw -pl catalog-service/ spring-boot:build-image -DskipTests
  #notification-service api-gateway catalog-service order-service
}

function start_infra() {
    docker compose -f ${dc_infra} up -d
    docker compose -f ${dc_infra} logs -f
}

function stop_infra() {
    docker compose -f ${dc_infra} down
}

function start_app() {
    docker compose -f ${dc_app} up -d
    docker compose -f ${dc_app} logs -f
}

function stop_app() {
    docker compose -f ${dc_app} down
}

function start_mon() {
    docker compose -f ${dc_mon} up -d
    docker compose -f ${dc_mon} logs -f
}

function stop_mon() {
    docker compose -f ${dc_mon} down
}

function start_build() {
  build_image
  start
}

function start() {
  start_infra
  start_app
  start_mon
}

function stop() {
    stop_infra
    stop_app
    stop_mon
}

function restart() {
    stop
    start
}

# ---------------------------------

ACTION=${1:-start}

eval ${ACTION}
