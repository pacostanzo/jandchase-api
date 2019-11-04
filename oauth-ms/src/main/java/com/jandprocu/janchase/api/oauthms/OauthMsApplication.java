package com.jandprocu.janchase.api.oauthms;

import com.jandprocu.janchase.api.oauthms.config.RibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@RibbonClient(name = "server", configuration = RibbonConfiguration.class)
public class OauthMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthMsApplication.class, args);
    }

}
