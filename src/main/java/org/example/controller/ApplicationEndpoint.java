package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.OrderRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApplicationEndpoint {

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequest request) {
        log.info("Order created");
    }

    @GetMapping("/orders/{orderId}")
    public void getOrder(@PathVariable String orderId) {
        log.info("Order " + orderId + " fetched successfully");
    }

    @GetMapping("/users/{userId}/profile")
    public void getUserProfile(@PathVariable String userId) {
        log.info("Profile for user {} " , userId);
    }
}