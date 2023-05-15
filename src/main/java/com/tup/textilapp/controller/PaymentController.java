package com.tup.textilapp.controller;

import com.mercadopago.exceptions.MPApiException;
import com.tup.textilapp.model.dto.RegisterPaymentDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(path = "{orderId}")
    public ResponseEntity<?> create(@PathVariable Integer orderId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        try {
            return ResponseEntity.ok(this.paymentService.createByOrder(orderId,token));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @PostMapping
    public ResponseEntity<?> registerPayment(@RequestBody RegisterPaymentDTO body) {
        try {
            this.paymentService.registerPayment(body);
            return ResponseEntity.ok(new ResponseMessageDTO("Payment registered succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "totalEarningsPerMonth")
    public ResponseEntity<?> getTotalEarningsPerMonth() {
        try {
            return ResponseEntity.ok(paymentService.getTotalEarningsPerMonth());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
    @GetMapping(path = "{paymentId}")
    public ResponseEntity<?> validatePayment(@PathVariable Long paymentId) {
        try {
            return ResponseEntity.ok(paymentService.validatePayment(paymentId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ResponseMessageDTO(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(e.getMessage()));
        } catch (MPApiException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getApiResponse());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessageDTO(e.getMessage()));
        }
    }
}
