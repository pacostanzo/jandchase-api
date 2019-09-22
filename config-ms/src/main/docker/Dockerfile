FROM openjdk:12-alpine

MAINTAINER Pablo Costanzo "costanzopa@gmail.com"

WORKDIR /usr/local/bin/

ADD maven/config-ms-0.0.1-SNAPSHOT.jar config-ms.jar

EXPOSE 8088

CMD ["java","-Dspring.profiles.active=docker", "-jar","config-ms.jar"]