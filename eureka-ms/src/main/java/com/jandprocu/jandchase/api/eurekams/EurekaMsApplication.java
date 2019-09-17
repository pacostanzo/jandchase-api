package com.jandprocu.jandchase.api.eurekams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMsApplication.class, args);
    }

}
