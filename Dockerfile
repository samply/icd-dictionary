FROM maven:3.8.1-openjdk-11 AS build

COPY ./ /workingdir/
WORKDIR /workingdir

# Build the JAR file. Use -B and -ntp options, to keep the log file
# under 1MB, because Docker has a built-in log file limit that
# kills the build otherwise.
RUN mvn clean
RUN mvn -B -ntp package

FROM openjdk:14-ea-alpine

COPY --from=build /workingdir/target/icd10-dictionary.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

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

COPY ./docker/start.sh .
RUN chmod +x ./start.sh
RUN mkdir config
COPY ./docker/docker.template.yml config

# Temp directory for uploading dictionary
RUN mkdir -p /var/tmp/icd10
RUN chmod a+rx /var/tmp/icd10

CMD ["./start.sh"]
