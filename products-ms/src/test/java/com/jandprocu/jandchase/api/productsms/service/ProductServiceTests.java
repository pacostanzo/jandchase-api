package com.jandprocu.jandchase.api.productsms.service;

import com.jandprocu.jandchase.api.productsms.exception.ProductNotCreatedException;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotFoundException;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotUpdatedException;
import com.jandprocu.jandchase.api.productsms.model.Product;
import com.jandprocu.jandchase.api.productsms.repository.ProductRepository;
import com.jandprocu.jandchase.api.productsms.rest.ProductRequest;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.rest.ProductRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    private IProductService productService;

    private ProductRequest productRequest;
    private Product product;

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        productService = new ProductService(productRepository);
        productRequest = new ProductRequest();
        productRequest.setName("Dell Inspiron");
        productRequest.setDescription("NoteBook Dell Inspiron 15");
        productRequest.setCategory("computer");
        productRequest.setAmount(900);
        productRequest.setCurrency("USD");

        product = modelMapper.map(productRequest, Product.class);

    }


    @Test
    public void createProduct_OK_ReturnsProductInfo() {
        //arrange
        given(productRepository.save(any())).willReturn(new Product());
        //act
        ProductRest productRest = productService.createProduct(productRequest);
        //assert
        assertThat(productRest.getName()).isEqualTo("Dell Inspiron");
        assertThat(productRest.getDescription()).isEqualTo("NoteBook Dell Inspiron 15");
        assertThat(productRest.getCategory()).isEqualTo("computer");
        assertThat(productRest.getAmount()).isEqualTo(900.0);
        assertThat(productRest.getCurrency()).isEqualTo("USD");
    }


    @Test(expected = ProductNotCreatedException.class)
    public void createUser_WhenUserWasCreated() throws DataAccessException {
        given(productRepository.save(any())).willThrow(new DuplicateKeyException("Test Exception"));
        productService.createProduct(productRequest);
    }

    @Test
    public void getProductByProductId_OK_ReturnsProductInfo() {

        given(productRepository.findByProductId(anyString())).willReturn(product);

        ProductRest productRest = productService.getProductByProductId("userId");

        assertThat(productRest.getName()).isEqualTo("Dell Inspiron");
        assertThat(productRest.getDescription()).isEqualTo("NoteBook Dell Inspiron 15");
        assertThat(productRest.getCategory()).isEqualTo("computer");
        assertThat(productRest.getAmount()).isEqualTo(900.0);
        assertThat(productRest.getCurrency()).isEqualTo("USD");
    }

    @Test(expected = ProductNotFoundException.class)
    public void getProductByProductId_WhenProductNotFound() {
        given(productRepository.findByProductId(anyString())).willReturn(null);
        productService.getProductByProductId(anyString());
    }

    @Test
    public void updateProductByProductId_OK_ReturnsProductUpdated() {

        ProductRequest productToUpdate = new ProductRequest();
        productToUpdate.setName("Dell Inspiron");
        productToUpdate.setDescription("NoteBook Dell Inspiron 15");
        productToUpdate.setCategory("computer");
        productToUpdate.setAmount(1000);
        productToUpdate.setCurrency("USD");

        given(productRepository.findByProductId(anyString())).willReturn(product);

        ProductResponse productResponse = productService.updateProductByProductId("userId", productToUpdate);

        assertThat(productResponse.getName()).isEqualTo("Dell Inspiron");
        assertThat(productResponse.getAmount()).isEqualTo(1000);
        assertThat(productResponse.getCurrency()).isEqualTo("USD");

    }

    @Test(expected = ProductNotFoundException.class)
    public void updateProductByProductId_WhenProductNotFound() {
        given(productRepository.findByProductId(anyString())).willReturn(null);
        productService.updateProductByProductId(anyString(), new ProductRequest());
    }

    @Test(expected = ProductNotUpdatedException.class)
    public void updateProductByProductId_WhenProductCouldNotBeUpdated() {

        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Dell Inspiron");
        updateRequest.setDescription("NoteBook Dell Inspiron 15");
        updateRequest.setCategory("computer");
        updateRequest.setAmount(1000);
        updateRequest.setCurrency("USD");

        given(productRepository.findByProductId(anyString())).willReturn(product);
        given(productRepository.save(any())).willThrow(new DuplicateKeyException("Test Exception"));
        productService.updateProductByProductId(anyString(), updateRequest);
    }


    @Test
    public void deleteProductByProductId_OK_ReturnsProductDeleted() {

        given(productRepository.findByProductId(anyString())).willReturn(product);

        ProductResponse userRest = productService.deleteProductByProductId("userId");

        assertThat(userRest.getName()).isEqualTo("Dell Inspiron");
        assertThat(userRest.getAmount()).isEqualTo(900);
        assertThat(userRest.getCurrency()).isEqualTo("USD");
    }


    @Test(expected = ProductNotFoundException.class)
    public void deleteProductByProductId_WhenProductNotFound() {
        given(productRepository.findByProductId(anyString())).willReturn(null);
        productService.deleteProductByProductId(anyString());
    }

}
