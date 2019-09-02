package com.jandprocu.jandchase.api.productsms.service;

import com.jandprocu.jandchase.api.productsms.rest.ProductRequestByIds;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponsePageable;
import com.jandprocu.jandchase.api.productsms.rest.ProductRest;

import java.util.Map;

public interface IProductService {

    ProductResponse createProduct(ProductRest userRequest);

    ProductResponse getProductByProductId(String productId);

    ProductResponse updateProductByProductId(String productId, ProductRest updateRequest);

    ProductResponse partialUpdateProductByProductId(String productId, Map<String, Object> updateRequest);

    ProductResponse deleteProductByProductId(String productId);

    ProductResponsePageable getAllProductsByProductId(ProductRequestByIds requestByIds, int pageNo, int pageSize, String sortBy);

    ProductResponsePageable getAllProducts(String name, int pageNo, int pageSize, String sortBy);
}
