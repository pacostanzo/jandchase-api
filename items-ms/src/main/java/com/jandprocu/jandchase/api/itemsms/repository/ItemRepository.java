package com.jandprocu.jandchase.api.itemsms.repository;

import com.jandprocu.jandchase.api.itemsms.model.Item;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    Item findByItemId(String itemId);
}
