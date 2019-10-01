FROM openjdk:12-alpine

MAINTAINER Pablo Costanzo "costanzopa@gmail.com"

WORKDIR /usr/local/bin/

ADD maven/users-ms-0.0.1-SNAPSHOT.jar users-ms.jar

CMD ["java","-Dspring.profiles.active=docker", "-jar","users-ms.jar"]