package com.example.demo.controllers;

import com.example.demo.models.ProductoModel;
import com.example.demo.services.RequestService;
import com.example.demo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private RequestService requestService;

    // Obtener los productos solo si la solicitud del minorista fue aceptada
    @GetMapping("/getProductos")
    public ResponseEntity<List<ProductoModel>> getProducts(@RequestParam Long retailerId, @RequestParam Long wholesalerId) {
        // Verificamos si el minorista tiene acceso a los productos del mayorista
        boolean hasAccess = requestService.hasAccess(retailerId, wholesalerId);

        if (hasAccess) {
            List<ProductoModel> products = productoService.getAllProducts();
            return ResponseEntity.ok(products);
        } else {
            return ResponseEntity.status(403).body(null);  // 403 Forbidden
        }
    }
}
