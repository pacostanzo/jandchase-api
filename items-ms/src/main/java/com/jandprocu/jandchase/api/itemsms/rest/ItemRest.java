package com.jandprocu.jandchase.api.itemsms.rest;

public class ItemRest  {
    private int quantity;
    private double total;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
