package com.example.demo.services;

import com.example.demo.models.ProductoModel;
import com.example.demo.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos los productos del mayorista
    public List<ProductoModel> getAllProducts() {
        return productoRepository.findAll();
    }
}

