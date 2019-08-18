package com.jandprocu.jandchase.api.productsms.repository;


import com.jandprocu.jandchase.api.productsms.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getByProductId_OK_ReturnsProductDetails() {

        Product productToStore = new Product();
        productToStore.setName("Dell Inspiron");
        productToStore.setProductId(String.valueOf(UUID.randomUUID()));
        productToStore.setCreatedAt(new Date());
        productToStore.setDescription("Dell Inspiron Description");
        productToStore.setCategory("computer");
        productToStore.setAmount(900);
        productToStore.setCurrency("USD");


        Product savedProduct = entityManager.persistAndFlush(productToStore);
        Product product = repository.findByProductId(productToStore.getProductId());

        assertThat(product.getName()).isEqualTo(savedProduct.getName());
    }
}
