FROM openjdk:12-alpine

MAINTAINER Pablo Costanzo "costanzopa@gmail.com"

WORKDIR /usr/local/bin/

ADD maven/oauth-ms-0.0.1-SNAPSHOT.jar oauth-ms.jar


CMD ["java","-Dspring.profiles.active=docker", "-jar","oauth-ms.jar"]