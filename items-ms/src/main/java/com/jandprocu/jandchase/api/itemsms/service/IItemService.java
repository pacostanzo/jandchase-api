package com.jandprocu.jandchase.api.itemsms.service;

import com.jandprocu.jandchase.api.itemsms.rest.ItemRequest;
import com.jandprocu.jandchase.api.itemsms.rest.ItemResponse;

public interface IItemService {

    ItemResponse createItem(ItemRequest itemRest);
}
