package com.timeissecuritytime.controller;

import com.timeissecuritytime.entity.Product;
import com.timeissecuritytime.entity.Sale;
import com.timeissecuritytime.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @GetMapping("/sales/{productId}")
    public ResponseEntity<List<Sale>> getSalesByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.findSalesByProductId(productId));
    }
}