package com.tup.textilapp.controller;

import com.tup.textilapp.service.PaymentMethodService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }
    @GetMapping
    public ResponseEntity<?> getAll() {
            return ResponseEntity.ok(this.paymentMethodService.getAll());
    }
}
