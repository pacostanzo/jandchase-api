package com.jandprocu.jandchase.api.itemsms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.jandprocu.jandchase.api.itemsms.rest.ItemRequest;
import com.jandprocu.jandchase.api.itemsms.rest.ItemResponse;
import com.jandprocu.jandchase.api.itemsms.rest.product.ProductResponse;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith (SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {ItemsMsApplicationTests.LocalRibbonClientConfiguration.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsMsApplicationTests {

	@Autowired
	TestRestTemplate testRestTemplate;

	@LocalServerPort
	int randomServerPort;

	@Value("${local.server.url}")
	String localHost;

	@Autowired
	private ObjectMapper objectMapper;

	private ItemResponse itemResponse;
	private ItemRequest itemRequest;
	private MultiValueMap<String, String> headers;

	private final String ITEM_ID = "TEST_ID_1";
	private final String PRODUCT_ID = "TEST_ID_PD_1";

	@ClassRule
	public static WireMockClassRule wiremock = new WireMockClassRule(WireMockConfiguration.options().dynamicPort());


	@Before
	public void setUp() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		itemRequest = new ItemRequest();
		itemRequest.setQuantity(10);
		itemRequest.setProductId(PRODUCT_ID);

		itemResponse = modelMapper.map(itemRequest, ItemResponse.class);

		headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", "application/json");
	}


	@Test
	public void a_createItem_OK_ReturnsItemDetails() throws JsonProcessingException {
		//arrange
		HttpEntity<ItemRequest> request = new HttpEntity<>(itemRequest);
		ProductResponse productResponse = new ProductResponse();
		productResponse.setProductId(PRODUCT_ID);
		productResponse.setName("Dell Inspiron");
		productResponse.setDescription("Updated Description.");
		productResponse.setCategory("computer");
		productResponse.setAmount(1000);
		productResponse.setCurrency("USD");

		stubFor(get(urlEqualTo("/products/"+PRODUCT_ID))
				.willReturn(aResponse().withStatus(201)
										.withHeader("Content-Type", "application/json")
										.withBody(objectMapper.writeValueAsString(productResponse))));


		//act
		ResponseEntity<ItemResponse> response = testRestTemplate.postForEntity(
				localHost + randomServerPort + "/items", request, ItemResponse.class);

		//assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getProduct().getProductId()).isEqualTo(PRODUCT_ID);
		assertThat(response.getBody().getQuantity()).isEqualTo(10);
		assertThat(response.getBody().getTotal()).isEqualTo(10000);
	}


	@TestConfiguration
	public static class LocalRibbonClientConfiguration {

		@Bean
		public ServerList<Server> ribbonServerList() {
			return new StaticServerList<>(new Server("localhost", wiremock.port()));
		}
	}

}
