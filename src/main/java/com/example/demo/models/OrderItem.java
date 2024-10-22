package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")  // Indica que este campo será una clave foránea que apunta a la orden
    private OrderModel order;

    @ManyToOne
    @JoinColumn(name = "product_id_producto")  // Clave foránea que apunta al producto
    private ProductoModel product;

    private int quantity;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public ProductoModel getProduct() {
        return product;
    }

    public void setProduct(ProductoModel product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
