# Example of a full Microservice Architecture with Spring Boot, Spring Cloud, Zuul and Eureka.

[![Build Status](https://circleci.com/gh/costanzopa/jandchase-api/tree/master.svg?style=svg)](https://circleci.com/gh/costanzopa/jandchase-api/tree/master)
[![MIT Licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/costanzopa/jandchase-api/master/LICENSE.txt)

Sample microservices application for managing products and shopping lists using:

- **Spring Boot:** Framework for creating standalone Java applications.
- **Jackson:** JSON parser for Java.
- **Netflix Zuul:** API gateway.
- **Netflix Eureka:** Service discovery.
- **RabbitMQ:** Message broker.
- **MongoDB:** NoSQL database based on documents.

This application consists of four differents services:

- **Product service:** Provides API for managing products. By default it runs on port `8001`.
- **Service discovery:** Netflix Eureka service that discovers and registers other service instances. By default it runs on port `8761`.
- **API gateway:** Netflix Zuul API gateway that sits on the top of the product and shopping list services, providing a gateway for those services. By default it runs on port `8765`.

See the diagram below:

<!-- Hack to center the image in GitHub -->
<p align="center">
  <img src="misc/jandchase-architecture.png" alt="Architecture diagram" width="65%"/>
</p>