package com.example.springboot.controllers;

import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/api/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping(value = "/api/products/{id}")
    public Product getProduct(@PathVariable int id) {
        return productRepository.findById(id);
    }

    @PostMapping(value = "/api/products", produces = "application/json")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}
