package com.jandprocu.jandchase.api.productsms.repository;

import com.jandprocu.jandchase.api.productsms.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository  extends PagingAndSortingRepository<Product, Long> {

    Product findByProductId(String productId);
}
