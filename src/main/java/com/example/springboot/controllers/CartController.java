package com.example.springboot.controllers;

import com.example.springboot.models.Cart;
import com.example.springboot.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping(value = "/api/carts")
    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }
}
