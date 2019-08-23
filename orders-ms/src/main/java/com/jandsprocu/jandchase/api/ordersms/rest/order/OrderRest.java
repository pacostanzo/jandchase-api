package com.jandsprocu.jandchase.api.ordersms.rest.order;

import java.util.Arrays;
import java.util.List;

public abstract class OrderRest {

    private double totalAmount;
    private String totalCurrency;
    private List<ItemRest> items;

    public OrderRest() {
        this.items = Arrays.asList();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalCurrency(String currency) {
        this.totalCurrency = currency;
    }

    public String getTotalCurrency() {
        return totalCurrency;
    }

    public void addItem(ItemRest item) {
        this.items.add(item);
    }

    public ItemRest getItem(int index) {
        return items.get(index);
    }

}
