package com.jandsprocu.jandchase.api.ordersms.rest.product;

import com.jandsprocu.jandchase.api.ordersms.rest.product.ProductRest;

public class ProductResponse extends ProductRest {
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
