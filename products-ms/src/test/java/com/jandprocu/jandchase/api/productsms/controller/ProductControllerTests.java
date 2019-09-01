package com.jandprocu.jandchase.api.productsms.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotCreatedException;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotFoundException;
import com.jandprocu.jandchase.api.productsms.exception.ProductNotUpdatedException;
import com.jandprocu.jandchase.api.productsms.rest.ProductRequest;
import com.jandprocu.jandchase.api.productsms.rest.ProductRequestByIds;
import com.jandprocu.jandchase.api.productsms.rest.ProductResponse;
import com.jandprocu.jandchase.api.productsms.service.IProductService;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private ProductResponse productUpdateResponse;

    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        productRequest = new ProductRequest();
        productUpdateResponse = new ProductResponse();
        productUpdateResponse.setName("Dell Inspiron");
        productRequest.setName("Dell Inspiron");
        productRequest.setDescription("NoteBook Dell Inspiron 15");
        productUpdateResponse.setDescription("Updated Description.");
        productRequest.setCategory("computer");
        productUpdateResponse.setCategory("computer");
        productRequest.setAmount(900);
        productRequest.setCurrency("USD");
        productUpdateResponse.setAmount(900);
        productUpdateResponse.setCurrency("USD");

        productResponse = modelMapper.map(productRequest, ProductResponse.class);


    }


    @Test
    public void createProduct_ShouldReturnCreatedProduct() throws Exception {
        //arrange
        Mockito.when(productService.createProduct(any())).thenReturn(productResponse);
        //act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated()).andReturn();

        //assert
        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo(objectMapper.writeValueAsString(productResponse));
    }


    @Test
    public void createProduct_NotCreatedBecauseConflict() throws Exception {
        given(productService.createProduct(any())).willThrow(new ProductNotCreatedException());

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isConflict());
    }


    @Test
    public void createRequestWithEmptyValues_ShouldReturnAConstraintException() throws Exception {
        //arrange
        Mockito.when(productService.createProduct(any())).thenReturn(productResponse);
        //act
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProductRequest())))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void getProduct_ShouldReturnProduct() throws Exception {

        given(productService.getProductByProductId(anyString())).willReturn(productResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/asdefasfdsdfdscsa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Dell Inspiron"))
                .andExpect(jsonPath("description").value("NoteBook Dell Inspiron 15"))
                .andExpect(jsonPath("category").value("computer"));
    }

    @Test
    public void getProduct_NotFound() throws Exception {
        given(productService.getProductByProductId(anyString())).willThrow(new ProductNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/products/pacostanzo@gmail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProductByIds_ShouldReturnProduct() throws Exception {

        //arrange
        List<ProductResponse> productsResponse = Arrays.asList(productResponse);
        Mockito.when(productService.getAllProductsByProductId(any(), anyInt(), anyInt(), anyString())).thenReturn(productsResponse);
        //act
        ProductRequestByIds productRequestByIds = new ProductRequestByIds("PRODUCT_TD_1");
        mockMvc.perform(MockMvcRequestBuilders.post("/products/getByIds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequestByIds)))
                .andExpect(status().isOk());
    }


    @Test
    public void updateProductFull_ShouldReturnAProductUpdated() throws Exception {

        given(productService.updateProductByProductId(anyString(), any())).willReturn(productUpdateResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/productId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Dell Inspiron"))
                .andExpect(jsonPath("description").value("Updated Description."))
                .andExpect(jsonPath("category").value("computer"));
    }


    @Test
    public void updateProductFull_NotFound() throws Exception {

        given(productService.updateProductByProductId(anyString(), any())).willThrow(new ProductNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/products/productId")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateProductFull_BadRequest() throws Exception {

        given(productService.updateProductByProductId(anyString(), any())).willThrow(new ProductNotUpdatedException());

        mockMvc.perform(MockMvcRequestBuilders.put("/products/productId")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteProduct_ShouldDeleteTheProduct() throws Exception {
        //arrange
        given(productService.deleteProductByProductId(anyString())).willReturn(new ProductResponse());
        //act
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/productId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
