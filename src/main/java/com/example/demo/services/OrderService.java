package com.example.demo.services;

import com.example.demo.models.OrderItem;
import com.example.demo.models.OrderModel;
import com.example.demo.models.PaymentModel;
import com.example.demo.models.ProductoModel;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.PaymentRepository;
import com.example.demo.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Definir el número de cuenta como una constante
    private static final String ACCOUNT_NUMBER = "2020-4563-7894";

    public String createOrder(Long retailerId, List<OrderItem> items) {
        double total = 0;

        // Crear la instancia de la orden
        OrderModel order = new OrderModel();
        order.setRetailerId(retailerId);
        order.setAccountNumber(ACCOUNT_NUMBER);  // Asignar el número de cuenta a la orden

        // Calcular el total de la orden, sin modificar el inventario aún
        for (OrderItem item : items) {
            ProductoModel product = productoRepository.findById(item.getProduct().getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Solo validamos que haya suficiente stock
            if (product.getCantidad() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getNombre());
            }

            // Calcular el total de la orden
            total += product.getPrecio() * item.getQuantity();

            // Asignar la orden a cada item
            item.setOrder(order);
        }

        // Guardar la orden con los ítems
        order.setItems(items);
        order.setTotalAmount(total);  // Asignar el monto total de la orden
        order.setStatus("PENDIENTE_PAGO"); // La orden empieza con el estado "PENDIENTE_PAGO"
        order.setAmountPaid(0);  // Inicializar con 0 el monto pagado
        orderRepository.save(order);

        return "Orden creada con éxito. Total: $" + total + ". Número de cuenta para el pago: " + ACCOUNT_NUMBER;
    }

    // Método para verificar el pago de una orden
    public String verifyPayment(Long orderId, double amountPaid) {
        // Buscar la orden en la base de datos
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Sumar el monto pagado al monto total pagado hasta el momento
        double totalAmount = order.getTotalAmount();
        double newTotalPaid = order.getAmountPaid() + amountPaid;  // Acumular el pago
        String message;

        if (newTotalPaid >= totalAmount) {
            // Pago correcto o pago en exceso
            order.setStatus("PAGO_CORRECTO");

            // Actualizar inventario solo si el pago es completo o en exceso
            for (OrderItem item : order.getItems()) {
                ProductoModel product = productoRepository.findById(item.getProduct().getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Descontar del inventario
                product.setCantidad(product.getCantidad() - item.getQuantity());
                productoRepository.save(product);
            }

            if (newTotalPaid > totalAmount) {
                // Pago en exceso
                message = "Pago en exceso. Monto a devolver: $" + (newTotalPaid - totalAmount);
            } else {
                // Pago correcto
                message = "Pago realizado correctamente.";
            }

            order.setAmountPaid(totalAmount);  // Asegurarse de que el monto pagado sea el total
        } else {
            // Pago insuficiente, no se modifica el inventario
            order.setStatus("PAGO_INSUFICIENTE");
            message = "Pago insuficiente. Monto faltante: $" + (totalAmount - newTotalPaid);
            order.setAmountPaid(newTotalPaid);  // Actualizar el monto pagado acumulado
        }

        // Guardar el estado actualizado de la orden
        orderRepository.save(order);

        // Registrar el pago en la base de datos
        PaymentModel payment = new PaymentModel();
        payment.setOrderId(orderId);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentDate(new Date());
        paymentRepository.save(payment);

        return message;
    }

    // Método para obtener el número de cuenta
    public String getAccountNumber() {
        return ACCOUNT_NUMBER;
    }
}


