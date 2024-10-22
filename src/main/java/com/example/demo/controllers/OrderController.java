package com.example.demo.controllers;

import com.example.demo.models.OrderRequest;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //Para crear la ordene de compra
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            String message = orderService.createOrder(orderRequest.getRetailerId(), orderRequest.getItems());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //Para mostrar el Numero de cuenta
    @GetMapping("/account")
    public String getAccountNumber() {
        return "El número de cuenta para pagos es: " + orderService.getAccountNumber();
    }

}