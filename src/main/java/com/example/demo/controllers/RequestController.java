package com.example.demo.controllers;

import com.example.demo.models.Request;
import com.example.demo.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService requestService;

    // Endpoint para que el minorista solicite acceso
    @PostMapping("/requestAccess")
    public ResponseEntity<String> requestAccess(@RequestParam Long retailerId, @RequestParam Long wholesalerId) {
        Request request = requestService.createRequest(retailerId, wholesalerId);
        return ResponseEntity.ok("Solicitud creada con estado: " + request.getStatus());
    }

    // Endpoint para que el mayorista acepte una solicitud
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId) {
        requestService.updateRequestStatus(requestId, "ACCEPTED");
        return ResponseEntity.ok("Solicitud aceptada.");
    }

    // Endpoint para que el mayorista deniegue una solicitud
    @PostMapping("/deny/{requestId}")
    public ResponseEntity<String> denyRequest(@PathVariable Long requestId) {
        requestService.updateRequestStatus(requestId, "DENIED");
        return ResponseEntity.ok("Solicitud denegada.");
    }

    // Verificar si un minorista tiene acceso a los productos
    @GetMapping("/hasAccess")
    public ResponseEntity<Boolean> hasAccess(@RequestParam Long retailerId, @RequestParam Long wholesalerId) {
        boolean access = requestService.hasAccess(retailerId, wholesalerId);
        return ResponseEntity.ok(access);
    }
}