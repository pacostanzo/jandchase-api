FROM openjdk:12-alpine

MAINTAINER Pablo Costanzo "costanzopa@gmail.com"

WORKDIR /usr/local/bin/

ADD maven/products-ms-0.0.1-SNAPSHOT.jar products-ms.jar

CMD ["java","-Dspring.profiles.active=docker", "-jar","products-ms.jar"]