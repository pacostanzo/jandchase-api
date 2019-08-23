package com.jandsprocu.jandchase.api.ordersms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.jandsprocu.jandchase.api.ordersms.rest.order.ItemRequest;
import com.jandsprocu.jandchase.api.ordersms.rest.order.OrderRequest;
import com.jandsprocu.jandchase.api.ordersms.rest.order.OrderResponse;
import com.jandsprocu.jandchase.api.ordersms.rest.product.ProductResponse;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith (SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {OrdersMsApplicationTests.LocalRibbonClientConfiguration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdersMsApplicationTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Value("${local.server.url}")
    String localHost;

    @Autowired
    private ObjectMapper objectMapper;


    private OrderRequest orderRequestBody;
    private ItemRequest itemRequest_1;
    private ItemRequest itemRequest_2;
    private ProductResponse productResponse_1;
    private ProductResponse productResponse_2;
    private MultiValueMap<String, String> headers;



    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(WireMockConfiguration.options().dynamicPort());


    @Before
    public void setUp() {
        orderRequestBody = new OrderRequest();
        orderRequestBody.setTotalAmount(8000);
        orderRequestBody.setTotalCurrency("USD");
        itemRequest_1 = new ItemRequest();
        itemRequest_2 = new ItemRequest();
        itemRequest_1.setProductId("PRODUCT_ID_1");
        itemRequest_1.setQuantity(2);
        itemRequest_2.setProductId("PRODUCT_ID_2");
        itemRequest_2.setQuantity(3);
        orderRequestBody.addItem(itemRequest_1);
        orderRequestBody.addItem(itemRequest_2);

        productResponse_1 = new ProductResponse();
        productResponse_1.setProductId("PRODUCT_ID_1");
        productResponse_1.setName("Dell Inspiron");
        productResponse_1.setDescription("Dell Description.");
        productResponse_1.setCategory("computer");
        productResponse_1.setAmount(1000);
        productResponse_1.setCurrency("USD");

        productResponse_2 = new ProductResponse();
        productResponse_2.setProductId("PRODUCT_ID_2");
        productResponse_2.setName("MacBook Pro");
        productResponse_2.setDescription("Mac Description.");
        productResponse_2.setCategory("computer");
        productResponse_2.setAmount(2000);
        productResponse_2.setCurrency("USD");

        headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
    }

    @Test
    public void a_createItem_OK_ReturnsItemDetails() throws JsonProcessingException {
        //arrange
        HttpEntity<OrderRequest> request = new HttpEntity<>(orderRequestBody);
        List<ProductResponse> productsResponse = Arrays.asList(productResponse_1, productResponse_2);
        stubFor(get(urlEqualTo("/products/"+"PRODUCT_ID_1,PRODUCT_ID_1"))
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(productsResponse))));


        //act
        ResponseEntity<OrderResponse> response = testRestTemplate.postForEntity(
                localHost + randomServerPort + "/orders", request, OrderResponse.class);

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getItem(0).getProductId()).isEqualTo("PRODUCT_ID_1");
        assertThat(response.getBody().getItem(1).getProductId()).isEqualTo("PRODUCT_ID_2");
        assertThat(response.getBody().getTotalAmount()).isEqualTo(8000);
        assertThat(response.getBody().getTotalCurrency()).isEqualTo(10000);
    }

    @TestConfiguration
    public static class LocalRibbonClientConfiguration {

        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(new Server("localhost", wiremock.port()));
        }
    }
}
