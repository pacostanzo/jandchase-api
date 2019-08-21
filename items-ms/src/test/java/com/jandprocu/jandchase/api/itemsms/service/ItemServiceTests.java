package com.jandprocu.jandchase.api.itemsms.service;

import com.jandprocu.jandchase.api.itemsms.model.Item;
import com.jandprocu.jandchase.api.itemsms.repository.ItemRepository;
import com.jandprocu.jandchase.api.itemsms.rest.ItemRequest;
import com.jandprocu.jandchase.api.itemsms.rest.ItemResponse;
import com.jandprocu.jandchase.api.itemsms.rest.product.ProductResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTests {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProductClientService productClientService;

    private IItemService itemService;

    private ItemRequest itemRequest;
    private Item item;
    private String PRODUCT_ID = "PRODUCT_ID_1";
    private ProductResponse productResponse;


    @Before
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        itemService = new ItemService(itemRepository, productClientService);
        itemRequest = new ItemRequest();
        itemRequest.setProductId(PRODUCT_ID);
        itemRequest.setQuantity(10);
        itemRequest.setTotal(9000);
        productResponse = new ProductResponse();
        productResponse.setProductId(PRODUCT_ID);
        productResponse.setName("Dell Inspiron");
        productResponse.setDescription("Updated Description.");
        productResponse.setCategory("computer");
        productResponse.setAmount(900);
        productResponse.setCurrency("USD");

        item = modelMapper.map(itemRequest, Item.class);

    }


    @Test
    public void createItem_OK_ReturnsItemInfo() {
        //arrange
        given(itemRepository.save(any())).willReturn(new Item());
        given(productClientService.getProduct(PRODUCT_ID)).willReturn(productResponse);
        //act
        ItemResponse itemResponse = itemService.createItem(itemRequest);
        //assert
        assertThat(itemResponse.getProduct().getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(itemResponse.getQuantity()).isEqualTo(10);
        assertThat(itemResponse.getTotal()).isEqualTo(9000);
        assertThat(itemResponse.getProduct().getCurrency()).isEqualTo("USD");
    }
}
