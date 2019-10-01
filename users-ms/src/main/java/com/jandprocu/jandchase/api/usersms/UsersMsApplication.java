package com.jandprocu.jandchase.api.usersms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class UsersMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersMsApplication.class, args);
    }

}
