package com.jandprocu.jandchase.api.productsms.repository;

import com.jandprocu.jandchase.api.productsms.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository  extends PagingAndSortingRepository<Product, Long> {

    Product findByProductId(String productId);

    Page<Product> findByProductIdIn(List<String> productIds, Pageable paging);
}
