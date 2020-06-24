FROM openjdk:14-ea-alpine

ENV SPRING_PROFILES_ACTIVE=docker
ARG JAR_FILE=target/*.jar

ENV ICD_DB_HOST=localhost
ENV ICD_DB_PORT=5432
ENV ICD_DB_NAME=icd10
ENV ICD_DB_USER=postgres
ENV ICD_DB_PASSWORD=password
ENV ICD_POOL_SIZE=30

RUN apk update
RUN apk upgrade
RUN apk add bash
RUN apk add gettext

COPY ${JAR_FILE} app.jar
COPY ./docker/start.sh .
RUN chmod +x ./start.sh
RUN mkdir config
COPY ./docker/docker.template.yml config

CMD ["./start.sh"]
