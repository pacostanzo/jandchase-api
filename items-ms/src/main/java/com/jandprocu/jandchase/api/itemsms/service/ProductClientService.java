package com.jandprocu.jandchase.api.itemsms.service;

import com.jandprocu.jandchase.api.itemsms.rest.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "products-ws")
public interface ProductClientService {

    @GetMapping(path = "/products/{productId}")
    ProductResponse getProduct(@PathVariable String productId);
}
