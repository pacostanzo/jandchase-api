FROM openjdk:12-alpine

MAINTAINER Pablo Costanzo "costanzopa@gmail.com"

WORKDIR /usr/local/bin/

ADD maven/gateway-ms-0.0.1-SNAPSHOT.jar gateway-ms.jar

EXPOSE 8091

CMD ["java","-Dspring.profiles.active=docker", "-jar","gateway-ms.jar"]