package com.jandprocu.jandchase.api.productsms.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "product_id")
    @NotEmpty
    private String productId;

    @Column(nullable = false, length = 50)
    @NotEmpty
    private String name;

    private String description;

    @Column(nullable = false, length = 50)
    @NotEmpty
    private String category;

    @Column(precision = 8, scale = 2, nullable = false)
    private double amount;

    @Column(nullable = false, length = 5)
    @NotEmpty
    private String currency;

    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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



    public void setFieldValue(String fieldName, Object value) throws Exception {
        Class productClass = this.getClass();
        if (fieldName != null && !fieldName.isEmpty()) {
            String methodName = "update" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Class[] cArg = new Class[1];
            cArg[0] = Object.class;
            Method method = productClass.getDeclaredMethod(methodName, cArg);
            method.invoke(this, value);
        }
    }

    private void updateName(Object value) {
        this.setName(value.toString());
    }

    private void updateDesciption(Object value) {
        this.setDescription(value.toString());
    }

    private void updateCategory(Object value) {
        this.setCategory(value.toString());
    }

    private void updateAmount(Object value) {
        String toString = value.toString();
        Double doubleObject = Double.parseDouble(toString);
        this.setAmount(doubleObject.doubleValue());
    }

    private void updateCurrency(Object value) {
        this.setCurrency(value.toString());
    }

    private static final long serialVersionUID = 1L;
}
