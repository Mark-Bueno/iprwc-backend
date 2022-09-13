package com.example.springboot.controllers;

import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping(path = "{id}")
    public Product getProduct(@PathVariable int id) {
        return productRepository.findById(id);
    }

    @PostMapping(produces = "application/json")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @DeleteMapping(path = "{id}")
    public void deleteProduct(@PathVariable int id) {
        Product product = productRepository.findById(id);
        productRepository.delete(product);
    }
}
