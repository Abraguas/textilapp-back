package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.RegisterPaymentDTO;
import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(path = "payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(path = "{orderId}")
    public ResponseEntity<?> create(@PathVariable Integer orderId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(this.paymentService.createByOrder(orderId, token));
    }

    @PostMapping
    public ResponseEntity<?> registerPayment(@RequestBody RegisterPaymentDTO body) {
        this.paymentService.registerPayment(body);
        return ResponseEntity.ok(new ResponseMessageDTO("Payment registered succesfully"));
    }

    @GetMapping(path = "totalEarningsPerMonth")
    public ResponseEntity<?> getTotalEarningsPerMonth(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {
        return ResponseEntity.ok(paymentService.getTotalEarningsPerMonth(startDate, endDate));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchString
    ) {
        if ((pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
            return ResponseEntity.ok(this.paymentService.getAll());
        }
        if (!(pageNum == null || pageSize == null) && (searchString == null || searchString.length() < 1)) {
            return ResponseEntity.ok(this.paymentService.getAllByPageAndSize(pageNum, pageSize));
        }
        return ResponseEntity.ok(this.paymentService.getByUsernameAndPageAndSize(pageNum, pageSize, searchString));

    }

    @GetMapping(path = "{paymentId}")
    public ResponseEntity<?> validatePayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.validatePayment(paymentId));
    }
}
