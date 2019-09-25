package com.jandprocu.jandchase.api.productsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
public class ProductsMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsMsApplication.class, args);
    }

}
