package com.example.demo.models;

import jakarta.persistence.*;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long retailerId;  // ID del minorista que solicita acceso
    private Long wholesalerId;  // ID del mayorista (tú)

    private String status;  // PENDIENTE, ACEPTADA, DENEGADA

    // Constructor
    public Request() {}

    public Request(Long retailerId, Long wholesalerId, String status) {
        this.retailerId = retailerId;
        this.wholesalerId = wholesalerId;
        this.status = status;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public Long getWholesalerId() {
        return wholesalerId;
    }

    public void setWholesalerId(Long wholesalerId) {
        this.wholesalerId = wholesalerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
