package org.example.controller;

import org.example.entity.OrderRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApplicationEndpoint {

    @PostMapping("/orders")
    public String createOrder(@RequestBody OrderRequest request) {
        return "Order created";
    }

    @GetMapping("/orders/{orderId}")
    public String getOrder(@PathVariable String orderId) {
        return "Order " + orderId + " fetched successfully";
    }

    @GetMapping("/users/{userId}/profile")
    public String getUserProfile(@PathVariable String userId) {
        return "Profile for user " + userId;
    }
}