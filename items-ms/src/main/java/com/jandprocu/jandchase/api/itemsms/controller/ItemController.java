package com.jandprocu.jandchase.api.itemsms.controller;


import com.jandprocu.jandchase.api.itemsms.rest.ItemRequest;
import com.jandprocu.jandchase.api.itemsms.rest.ItemResponse;
import com.jandprocu.jandchase.api.itemsms.service.IItemService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("items")
public class ItemController {

    private IItemService itemService;
    private Environment environment;

    ItemController(IItemService itemService, Environment environment) {
        this.itemService = itemService;
        this.environment = environment;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ItemResponse> createUser(@Valid @RequestBody ItemRequest itemRequest) {
        ItemResponse createdItem = itemService.createItem(itemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

}
