package com.jandprocu.jandchase.api.productsms.controller;

import com.jandprocu.jandchase.api.productsms.rest.ProductRequest;
import com.jandprocu.jandchase.api.productsms.rest.ProductRequestByIds;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.service.IProductService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    private IProductService productService;
    private Environment environment;

    ProductController(IProductService productService, Environment environment) {
        this.productService = productService;
        this.environment = environment;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ProductResponse> createUser(@Valid @RequestBody ProductRequest createRequest) {
        ProductResponse createdProduct = productService.createProduct(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping(path = "/{productId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String productId) {
        ProductResponse productResponse = productService.getProductByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }


    @PostMapping(path = "/getByIds",
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<ProductResponse>> getAllProductsByProductId(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "productId") String sortBy,
            @Valid @RequestBody ProductRequestByIds requestByIds) {

        List<ProductResponse> productsResponse = productService.getAllProductsByProductId(requestByIds, pageNo, pageSize, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
    }

    @PutMapping(path = "/{productId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String productId, @Valid @RequestBody ProductRequest updateRequest) {
        ProductResponse updateResponse = productService.updateProductByProductId(productId, updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }

    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable String productId) {
        ProductResponse deleteProduct = productService.deleteProductByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(deleteProduct);
    }
}
