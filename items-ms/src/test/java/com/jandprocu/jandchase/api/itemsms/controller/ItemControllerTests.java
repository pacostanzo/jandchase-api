package com.jandprocu.jandchase.api.itemsms.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jandprocu.jandchase.api.itemsms.rest.ItemRequest;
import com.jandprocu.jandchase.api.itemsms.rest.ItemResponse;
import com.jandprocu.jandchase.api.itemsms.rest.product.ProductResponse;
import com.jandprocu.jandchase.api.itemsms.service.IItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemRequest itemRequest;
    private ItemResponse itemResponse;
    private ProductResponse productResponse;
    private String PRODUCT_ID = "PRODUCT_ID_1";

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        itemRequest = new ItemRequest();
        itemResponse = new ItemResponse();
        productResponse = new ProductResponse();
        itemRequest.setProductId(PRODUCT_ID);
        itemRequest.setQuantity(10);
        productResponse.setProductId(PRODUCT_ID);
        productResponse.setName("Dell Inspiron");
        productResponse.setDescription("Updated Description.");
        productResponse.setCategory("computer");
        productResponse.setAmount(900);
        productResponse.setCurrency("USD");
        itemResponse.setQuantity(10);
        itemResponse.setTotal(9000);
        itemResponse.setProduct(productResponse);
    }


    @Test
    public void createProduct_ShouldReturnCreatedProduct() throws Exception {
        //arrange
        Mockito.when(itemService.createItem(any())).thenReturn(itemResponse);
        //act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isCreated()).andReturn();

        //assert
        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo(objectMapper.writeValueAsString(itemResponse));
    }


}
