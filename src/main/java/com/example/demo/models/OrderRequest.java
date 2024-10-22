package com.example.demo.models;

import java.util.List;

public class OrderRequest {
    private Long retailerId;
    private List<OrderItem> items;

    // Getters y Setters
    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
