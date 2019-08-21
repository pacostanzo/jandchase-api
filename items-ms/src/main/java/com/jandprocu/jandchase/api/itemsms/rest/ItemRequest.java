package com.jandprocu.jandchase.api.itemsms.rest;

public class ItemRequest extends ItemRest {
    private String productId;


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
