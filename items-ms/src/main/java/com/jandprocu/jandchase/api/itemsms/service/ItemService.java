package com.jandprocu.jandchase.api.itemsms.service;

import com.jandprocu.jandchase.api.itemsms.exception.ItemNotCreatedException;
import com.jandprocu.jandchase.api.itemsms.model.Item;
import com.jandprocu.jandchase.api.itemsms.repository.ItemRepository;
import com.jandprocu.jandchase.api.itemsms.rest.ItemRequest;
import com.jandprocu.jandchase.api.itemsms.rest.ItemResponse;
import com.jandprocu.jandchase.api.itemsms.rest.ItemRest;
import com.jandprocu.jandchase.api.itemsms.rest.product.ProductResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;
    private ProductClientService productClient;
    private ModelMapper modelMapper;

    public ItemService(ItemRepository itemRepository, ProductClientService productClient) {
        this.productClient =  productClient;
        this.itemRepository = itemRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public ItemResponse createItem(ItemRequest itemRequest) {
        ProductResponse productResponse = productClient.getProduct(itemRequest.getProductId());
        ItemResponse itemResponse = new ItemResponse();
        if (productResponse != null) {
            Item itemEntity = modelMapper.map(itemRequest, Item.class);
            itemEntity.setItemId(UUID.randomUUID().toString());
            double total = productResponse.getAmount()*itemRequest.getQuantity();
            itemEntity.setTotal(total);
            try {
                itemRepository.save(itemEntity);
                itemResponse.setItemId(itemEntity.getItemId());
                itemResponse.setQuantity(itemEntity.getQuantity());
                itemResponse.setTotal(itemEntity.getTotal());
                itemResponse.setProduct(productResponse);
            } catch (DataAccessException exception) {
                throw new ItemNotCreatedException("Item " + itemEntity.getItemId() + " not created");
            }
        } else {
            throw new ItemNotCreatedException("Product: " + itemRequest.getProductId() + " not created");
        }
        return itemResponse;
    }
}
