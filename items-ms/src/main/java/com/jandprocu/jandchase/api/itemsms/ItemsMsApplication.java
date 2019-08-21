package com.jandprocu.jandchase.api.itemsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.jandprocu.jandchase.api")
@RibbonClient(name="products-ms")
public class ItemsMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemsMsApplication.class, args);
	}

}
