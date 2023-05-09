package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "payment-method")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;
    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(this.paymentMethodService.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
