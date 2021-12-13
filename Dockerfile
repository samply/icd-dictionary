FROM openjdk:17

COPY target/icd10-dictionary.jar /app/

WORKDIR /app
USER 1001

CMD ["java", "-jar", "icd10-dictionary.jar"]
