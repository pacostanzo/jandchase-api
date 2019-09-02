package com.jandprocu.jandchase.api.productsms.rest;

import java.util.ArrayList;
import java.util.List;

public class ProductResponsePageable {

    private List<ProductResponse> products;
    private int currentPageNumber;
    private int totalPages;

    public ProductResponsePageable(List<ProductResponse> products, int currentPageNumber, int totalPages) {
        this.products = products;
        this.currentPageNumber = currentPageNumber;
        this.totalPages = totalPages;
    }

    public ProductResponsePageable() {
        this.products = new ArrayList<>();
        this.currentPageNumber = 0;
        this.totalPages = 0;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
