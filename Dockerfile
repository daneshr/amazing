FROM openjdk:8-jdk-alpine

RUN addgroup -S amazing && adduser -S amazing -G amazing
USER amazing:amazing

ARG AMAZING_JAR=./target/amazing-0.0.1-SNAPSHOT.jar
ADD ${AMAZING_JAR} amazing.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=production","-jar","/amazing.jar"]