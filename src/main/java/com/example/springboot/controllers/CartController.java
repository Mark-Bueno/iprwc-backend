package com.example.springboot.controllers;

import com.example.springboot.models.Cart;
import com.example.springboot.models.Product;
import com.example.springboot.models.User;
import com.example.springboot.repositories.CartRepository;
import com.example.springboot.repositories.ProductRepository;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@RestController
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/api/carts")
    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }

    @GetMapping(value = "/api/carts/users/{userId}")
    public List<Cart> getUserCart(@PathVariable int userId) {
        LOGGER.info("Fetching cart by user: " + userId);
        List<Cart> userCartProducts = new ArrayList<>();
        List<Cart> allCartProducts = cartRepository.findAll();
        for (Cart cart : allCartProducts) {
            if (cart.getUser().getId() == userId) {
                userCartProducts.add(cart);
            }
        }
        return userCartProducts;
    }

    @PostMapping(value = "/api/carts/users/{userId}/products/{productId}")
    public Cart addProductInCart(@PathVariable int userId, @PathVariable int productId) {
        boolean productFound = false;
        List<Cart> allCartProducts = cartRepository.findAll();
        for (Cart c : allCartProducts) {
            if (c.getUser().getId() == userId && c.getProduct().getId() == productId) {
                System.out.println("Product found in cart");
                productFound = true;
                break;
            }
        }
        if (!productFound) {
            Cart cart = new Cart();
            User user = userRepository.findById(userId);
            cart.setUser(user);
            user.addCart(cart);
            Product product = productRepository.findById(productId);
            cart.setProduct(product);
            product.addCart(cart);
            cart.setAmount(1);
            System.out.println(cart.toString());
            return cartRepository.save(cart);
        }
        return null;
    }
}
