package com.jandprocu.jandchase.api.productsms;

import com.jandprocu.jandchase.api.productsms.rest.ProductRequest;
import com.jandprocu.jandchase.api.productsms.rest.ProductRequestByIds;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponsePageable;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductsMsApplicationTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Value("${local.server.url}")
    String localHost;

    private ProductRequest productRequest;
    private ProductRequest fullUpdateRequest;
    private MultiValueMap<String, String> headers;

    private final String PRODUCT_ID = "TEST_ID_1";

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        productRequest = new ProductRequest();
        productRequest.setName("Dell Inspiron");
        productRequest.setDescription("NoteBook Dell Inspiron 15");
        productRequest.setCategory("computer");
        productRequest.setAmount(900);
        productRequest.setCurrency("USD");

        fullUpdateRequest = modelMapper.map(productRequest, ProductRequest.class);

        headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
    }


    @Test
    public void a_createProduct_OK_ReturnsProductDetails() {
        //arrange
        HttpEntity<ProductRequest> request = new HttpEntity<>(productRequest);

        //act
        ResponseEntity<ProductResponse> response = testRestTemplate.postForEntity(
                localHost + randomServerPort + "/", request, ProductResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Dell Inspiron");
        assertThat(response.getBody().getDescription()).isEqualTo("NoteBook Dell Inspiron 15");
        assertThat(response.getBody().getAmount()).isEqualTo(900);
        assertThat(response.getBody().getCurrency()).isEqualTo("USD");
    }

    @Test
    public void b_getProductByProductId_OK_ReturnsProductDetails() {
        //act
        ResponseEntity<ProductResponse> response = testRestTemplate.exchange(
                localHost + randomServerPort + "/" + PRODUCT_ID, HttpMethod.GET, new HttpEntity<>(headers),
                ProductResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Mac Book Air");
        assertThat(response.getBody().getDescription()).isEqualTo("NoteBook Mac Book Air");
        assertThat(response.getBody().getAmount()).isEqualTo(1000);
        assertThat(response.getBody().getCurrency()).isEqualTo("USD");
    }

    @Test
    public void c_getProductsByProductIds_OK_ReturnsProductsDetails() {
        //arrange
        ProductRequestByIds requestByIds = new ProductRequestByIds("TEST_ID_1,TEST_ID_2,TEST_ID_3");
        HttpEntity<ProductRequestByIds> request = new HttpEntity<>(requestByIds, headers);

        //act
        ResponseEntity<ProductResponsePageable> response = testRestTemplate.exchange(
                localHost + randomServerPort + "/getByIds", HttpMethod.POST, request,
                ProductResponsePageable.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getProducts().get(0).getProductId()).isEqualTo("TEST_ID_1");
        assertThat(response.getBody().getProducts().get(1).getProductId()).isEqualTo("TEST_ID_2");
        assertThat(response.getBody().getProducts().get(2).getProductId()).isEqualTo("TEST_ID_3");
    }


    @Test
    public void d_updateAllProductFields_OK_ReturnUpdatedProductDetails() {
        //arrange
        HttpEntity<ProductRequest> request = new HttpEntity<>(fullUpdateRequest, headers);

        //act
        ResponseEntity<ProductResponse> response = testRestTemplate.exchange(
                localHost + randomServerPort + "/" + PRODUCT_ID,
                HttpMethod.PUT, request, ProductResponse.class);

        //assert

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Dell Inspiron");
        assertThat(response.getBody().getDescription()).isEqualTo("NoteBook Dell Inspiron 15");
        assertThat(response.getBody().getAmount()).isEqualTo(900);
        assertThat(response.getBody().getCurrency()).isEqualTo("USD");
    }


    @Test
    public void e_updatePartialProductFields_OK_ReturnUpdatedProductDetails() {
        HashMap<String, Object> partialUpdateRequest = new HashMap<>();
        partialUpdateRequest.put("category", "5th avenue");
        partialUpdateRequest.put("amount", "1000");

        //arrange
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(partialUpdateRequest, headers);
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        //act
        ResponseEntity<ProductResponse> response = testRestTemplate.exchange(
                localHost + randomServerPort + "/" + PRODUCT_ID,
                HttpMethod.PATCH, request, ProductResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCategory()).isEqualTo("5th avenue");
    }


    @Test
    public void f_deleteProductByProductId_OK() {
        //act
        testRestTemplate.exchange(localHost + randomServerPort + "/" + PRODUCT_ID,
                HttpMethod.DELETE, new HttpEntity<>(headers), String.class)
                .getStatusCode().equals(HttpStatus.OK);
    }


}
