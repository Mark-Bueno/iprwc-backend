package com.example.springboot.repositories;

import com.example.springboot.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    List<OrderProduct> findAll();
}
