package com.jandprocu.jandchase.api.productsms.rest;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class ProductRest {

    @NotNull(message = "Name cannot be missing or empty")
    @Size(min = 2, message = "Name must not be less than 2 characters")
    private String name;
    private String description;

    @NotNull(message = "Amount cannot be missing or empty")
    @DecimalMin(value = "0.00", message = "Amount must be a positive number")
    private double amount;

    @NotNull(message = "Currency cannot be missing or empty")
    @Size(min = 2, message = "Currency must not be less than 2 characters")
    private String currency;

    @NotNull(message = "Category cannot be missing or empty")
    @Size(min = 2, message = "Category must not be less than 2 characters")
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
