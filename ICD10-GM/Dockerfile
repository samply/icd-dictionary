FROM maven:3.8.1-openjdk-11 AS build

RUN mkdir -p /workingdir
WORKDIR /workingdir

RUN apt update -y && apt install -y git

# Clone fhir-claml repo
# Build the JAR file for fhir-claml
RUN git clone https://github.com/aehrc/fhir-claml.git && cd fhir-claml && mvn clean && mvn install

FROM openjdk:11-jdk-slim AS convert

RUN mkdir -p /workingdir
WORKDIR /workingdir

RUN apt update -y && apt install -y zip unzip

COPY --from=build /workingdir/fhir-claml/target/*.jar /workingdir
COPY run-fhir-claml-in-container.sh /workingdir/run-fhir-claml-in-container.sh
RUN chmod a+rx /workingdir/run-fhir-claml-in-container.sh

RUN ls -la /workingdir

