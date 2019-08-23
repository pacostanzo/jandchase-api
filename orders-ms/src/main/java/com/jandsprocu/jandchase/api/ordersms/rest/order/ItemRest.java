package com.jandsprocu.jandchase.api.ordersms.rest.order;

public class ItemRest {
    private String productId;
    private int quantity;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}
