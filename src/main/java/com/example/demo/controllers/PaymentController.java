package com.example.demo.controllers;

import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/verify")
    public String verifyPayment(@RequestParam Long orderId, @RequestParam double amountPaid) {
        // Lógica existente para verificar el pago
        String result = orderService.verifyPayment(orderId, amountPaid);

        // Si el pago se verificó correctamente, enviamos el webhook
        if ("Pago verificado con éxito".equals(result)) {
            sendWebhookNotification(orderId, amountPaid);
        }
        return result;
    }

    // Metodo que envía el webhook al sistema minorista
    private void sendWebhookNotification(Long orderId, double amountPaid) {
        // URL del endpoint del minorista que recibirá el webhook
        String urlMinorista = "http://172.16.12.242/api/porductos/webhook/payment-confirmed";

        // Construcción del payload del webhook
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("status", "paid");
        payload.put("amountPaid", amountPaid);
        payload.put("timestamp", System.currentTimeMillis());

        // Configuración del encabezado
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Creación de la solicitud
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        // Envío de la solicitud
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(urlMinorista, HttpMethod.POST, request, String.class);
            System.out.println("Notificación de pago enviada al minorista. Respuesta: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error enviando el webhook: " + e.getMessage());
            // Puedes agregar lógica de reintento aquí si es necesario
        }
    }
}
