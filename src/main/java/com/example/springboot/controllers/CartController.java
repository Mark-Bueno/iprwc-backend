package com.example.springboot.controllers;

import com.example.springboot.models.Cart;
import com.example.springboot.models.Product;
import com.example.springboot.models.User;
import com.example.springboot.repositories.CartRepository;
import com.example.springboot.repositories.ProductRepository;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@RestController
@RequestMapping("carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @GetMapping()
    public List<Cart> getUserCart() {
        User user = this.userController.getAuthenticatedUser();
        List<Cart> userCartProducts = new ArrayList<>();
        List<Cart> allCartProducts = cartRepository.findAll();
        for (Cart cart : allCartProducts) {
            if (cart.getUser().getId() == user.getId()) {
                userCartProducts.add(cart);
            }
        }
        return userCartProducts;
    }

    @PostMapping(value = "{productId}")
    public Cart addProductInCart(@PathVariable int productId) {
        int userId = this.userController.getAuthenticatedUser().getId();
        List<Cart> allCartProducts = cartRepository.findAll();
        for (Cart c : allCartProducts) {
            if (c.getUser().getId() == userId && c.getProduct().getId() == productId) {
                c.setAmount(c.getAmount() + 1);
                return cartRepository.save(c);
            }
        }
        Cart cart = new Cart();
        User user = userRepository.findById(userId);
        cart.setUser(user);
        Product product = productRepository.findById(productId);
        cart.setProduct(product);
        cart.setAmount(1);
        System.out.println(cart.toString());
        return cartRepository.save(cart);
    }

    @DeleteMapping()
    public void clearProductsInCart() {
        List<Cart> productsInCart = this.getUserCart();
        for (Cart c : productsInCart) {
            cartRepository.delete(c);
        }
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProductInCart(@PathVariable int productId) {
        List<Cart> productsInCart = this.getUserCart();
        for (Cart c : productsInCart) {
            if (productId == c.getProduct().getId()) {
                c.setAmount(c.getAmount() - 1);
                if (c.getAmount() == 0) {
                    cartRepository.delete(c);
                } else {
                    cartRepository.save(c);
                }
            }
        }
    }
}
