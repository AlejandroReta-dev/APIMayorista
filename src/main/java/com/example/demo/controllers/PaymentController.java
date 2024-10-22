package com.example.demo.controllers;

import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/verify")
    public String verifyPayment(@RequestParam Long orderId, @RequestParam double amountPaid) {
        return orderService.verifyPayment(orderId, amountPaid);
    }
}