package com.jandprocu.jandchase.api.productsms.service;

import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.rest.ProductRest;

public interface IProductService {

    ProductResponse createProduct(ProductRest userRequest);

    ProductResponse getProductByProductId(String productId);

    ProductResponse updateProductByProductId(String productId, ProductRest updateRequest);

    ProductResponse deleteProductByProductId(String productId);

}
