FROM gradle:8.2.1-jdk17 AS build
COPY build.gradle /opt/auth-server/build.gradle
COPY src /opt/auth-server/src
WORKDIR /opt/auth-server

RUN gradle clean build


FROM openjdk:17-jdk-slim

COPY --from=build /opt/auth-server/build/libs/auth-server.jar /opt/auth-server/auth-server.jar

CMD java $JAVA_OPTS -jar /opt/auth-server/auth-server.jar
EXPOSE 8080