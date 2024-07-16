package com.example.Ecom.controllers;

import com.example.Ecom.Services.OrdersService;
import com.example.Ecom.model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class orderController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable String id) {
        Optional<Orders> order = ordersService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Orders> createOrder(@RequestBody Orders orders) {
        return ResponseEntity.ok(ordersService.saveOrders(orders));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable String id, @RequestBody Orders order) {
        Optional<Orders> existingOrder = ordersService.getOrderById(id);
        if (existingOrder.isPresent()) {
            order.setId(id);
            return ResponseEntity.ok(ordersService.saveOrders(order));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
