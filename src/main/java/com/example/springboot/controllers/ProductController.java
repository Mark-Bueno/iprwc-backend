package com.example.springboot.controllers;

import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(produces = "application/json")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(path = "copy", produces = "application/json")
    public Product copyProduct(@RequestBody Product product) {
        product.setId(0);
        return productRepository.save(product);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping(produces = "application/json")
    public Product editProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(path = "{id}")
    public void deleteProduct(@PathVariable int id) {
        Product product = productRepository.findById(id);
        productRepository.delete(product);
    }
}
