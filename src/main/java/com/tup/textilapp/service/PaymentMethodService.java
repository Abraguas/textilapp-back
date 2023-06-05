package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.PaymentMethod;
import com.tup.textilapp.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> getAll() {
        return this.paymentMethodRepository.findAll();
    }
}
