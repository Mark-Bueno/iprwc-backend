package com.example.springboot.controllers;

import com.example.springboot.models.Cart;
import com.example.springboot.models.Order;
import com.example.springboot.models.OrderProduct;
import com.example.springboot.models.User;
import com.example.springboot.repositories.OrderProductRepository;
import com.example.springboot.repositories.OrderRepository;
import com.example.springboot.repositories.ProductRepository;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderpProductRepository;

    @Autowired
    private UserController userController;

    @Autowired
    private CartController cartController;

    @GetMapping()
    public List<Order> getUserOrders() {
        User user = this.userController.getAuthenticatedUser();
        List<Order> orders = new ArrayList<>();
        List<Order> allOrders = orderRepository.findAll();
        for (Order order : allOrders) {
            if (order.getUser().getId() == user.getId()) {
                orders.add(order);
            }
        }
        return orders;
    }

    @PostMapping()
    public void newOrder(@RequestBody Order order) {
        int userId = this.userController.getAuthenticatedUser().getId();
        Date date = new Date();
        order.setDate(date);
        order.setUser(this.userRepository.findById(userId));
        orderRepository.save(order);
        List<Cart> carts = this.cartController.getUserCart();
        for (Cart cart : carts) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProductName(cart.getProduct().name);
            orderProduct.setProductPhoto(cart.getProduct().imagePath);
            orderProduct.setOrder(order);
            orderProduct.setProductPrice(cart.getProduct().price);
            orderProduct.setAmount(cart.getAmount());
            orderpProductRepository.save(orderProduct);
        }
        this.cartController.clearProductsInCart();
    }

    @GetMapping(value = "{id}/products")
    public List<OrderProduct> getUserOrderProducts(@PathVariable int id) {
        int userId = this.userController.getAuthenticatedUser().getId();
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<OrderProduct> allOrderProducts = orderpProductRepository.findAll();
        for (OrderProduct orderProduct : allOrderProducts) {
            if (orderProduct.getOrder().getUser().getId() == userId && orderProduct.getOrder().getId() == id) {
                orderProducts.add(orderProduct);
            }
        }
        return orderProducts;
    }
}
