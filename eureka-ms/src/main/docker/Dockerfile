FROM openjdk:12-alpine

MAINTAINER Pablo Costanzo "costanzopa@gmail.com"

WORKDIR /usr/local/bin/

ADD maven/eureka-ms-0.0.1-SNAPSHOT.jar eureka-ms.jar

EXPOSE 8761

CMD ["java","-Dspring.profiles.active=docker", "-jar","eureka-ms.jar"]