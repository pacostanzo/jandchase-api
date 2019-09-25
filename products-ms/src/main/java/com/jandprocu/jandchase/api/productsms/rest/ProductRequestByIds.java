package com.jandprocu.jandchase.api.productsms.rest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

public class ProductRequestByIds {
    @Valid
    private List<String> productIds;

    public ProductRequestByIds() {
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
