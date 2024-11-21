package com.example.demo.controllers;

import com.example.demo.services.EmailService;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/verify")
    public String verifyPayment(@RequestParam Long orderId, @RequestParam double amountPaid) {
        // Lógica existente para verificar el pago
        String result = orderService.verifyPayment(orderId, amountPaid);

        // Enviar el webhook y correo solo si el pago fue correcto o en exceso
        if (result.equals("Pago realizado correctamente.") || result.startsWith("Pago en exceso")) {
            sendWebhookNotification(orderId, amountPaid);

            // Define aquí directamente el correo del minorista
            String emailMinorista = "alejandroreta4@gmail.com"; // Coloca tu correo aquí

            // Enviar correo
            if (emailMinorista != null && !emailMinorista.isEmpty()) {
                String subject = "Confirmación de pago de la orden #" + orderId;
                String body = "Estimado cliente,\n\n" +
                        "Hemos recibido el pago de su orden #" + orderId + " por un monto de " + amountPaid + ".\n" +
                        "Su pedido está en proceso. Gracias por su compra.\n\n" +
                        "Saludos cordiales,\nEquipo Mayorista";

                emailService.sendEmail(emailMinorista, subject, body);
            }
        }
        return result;
    }

    // Metodo que envía el webhook al sistema minorista
    private void sendWebhookNotification(Long orderId, double amountPaid) {
        String urlMinorista = "https://apiminorista-production-36d3.up.railway.app/api/productos/webhook/payment-confirmed";

        // Obtener los detalles de la orden, incluyendo los productos
        Map<String, Object> orderDetails = orderService.getOrderDetails(orderId);

        // Construcción del payload del webhook
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderDetails.get("orderId"));
        payload.put("status", "paid");
        payload.put("amountPaid", amountPaid);
        payload.put("timestamp", System.currentTimeMillis());
        payload.put("products", orderDetails.get("products")); // Agregar los productos

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
        }
    }
}
