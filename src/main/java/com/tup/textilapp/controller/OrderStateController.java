package com.tup.textilapp.controller;

import com.tup.textilapp.service.OrderStateService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orderState")
public class OrderStateController {
    private final OrderStateService orderStateService;

    public OrderStateController(OrderStateService orderStateService) {
        this.orderStateService = orderStateService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.orderStateService.getAll());
    }
}
