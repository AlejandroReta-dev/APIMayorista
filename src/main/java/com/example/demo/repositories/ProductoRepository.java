package com.example.demo.repositories;

import com.example.demo.models.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<ProductoModel, Integer> {
    Optional<ProductoModel> findBySku(String sku);
}
