package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.OrderDTO;
import com.tup.textilapp.model.entity.Order;
import com.tup.textilapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController (
            OrderService orderService
    ) {
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<?> registerOrder(@RequestBody OrderDTO orderDTO) {
        try {
            this.orderService.registerOrder(orderDTO);
            return ResponseEntity.ok("Order registered succesfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Order> lst = this.orderService.getAll();
            return ResponseEntity.ok(lst);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
