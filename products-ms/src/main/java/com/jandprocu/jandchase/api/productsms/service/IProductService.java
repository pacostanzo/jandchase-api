package com.jandprocu.jandchase.api.productsms.service;

import com.jandprocu.jandchase.api.productsms.rest.ProductRequestByIds;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.rest.ProductRest;

import java.util.List;

public interface IProductService {

    ProductResponse createProduct(ProductRest userRequest);

    ProductResponse getProductByProductId(String productId);

    ProductResponse updateProductByProductId(String productId, ProductRest updateRequest);

    ProductResponse deleteProductByProductId(String productId);

    List<ProductResponse> getAllProductsByProductId(ProductRequestByIds requestByIds, int pageNo, int pageSize, String sortBy);

    List<ProductResponse> getAllProducts(String name, int pageNo, int pageSize, String sortBy);
}
