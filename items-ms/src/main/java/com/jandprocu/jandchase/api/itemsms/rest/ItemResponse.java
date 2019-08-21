package com.jandprocu.jandchase.api.itemsms.rest;

import com.jandprocu.jandchase.api.itemsms.rest.product.ProductResponse;

public class ItemResponse extends ItemRest{

    private String itemId;
    private ProductResponse product;

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
