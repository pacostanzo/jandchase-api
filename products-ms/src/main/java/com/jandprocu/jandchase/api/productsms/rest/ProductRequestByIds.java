package com.jandprocu.jandchase.api.productsms.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductRequestByIds {
    private List<String> productIds;

    public ProductRequestByIds() {
        this.productIds = new ArrayList<>();
    }

    public ProductRequestByIds(String ids) {
        this.productIds = Arrays.asList(ids.split(","));
    }

    public void addId(String id) {
        this.productIds.add(id);
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
