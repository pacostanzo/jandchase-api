package com.jandprocu.jandchase.api.configms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigMsApplication.class, args);
    }

}
