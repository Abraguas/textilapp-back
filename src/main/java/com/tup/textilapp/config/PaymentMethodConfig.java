package com.tup.textilapp.config;


import com.tup.textilapp.model.entity.PaymentMethod;
import com.tup.textilapp.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMethodConfig implements CommandLineRunner {

    private final PaymentMethodRepository repository;
    @Value("${data.initialized}")
    private boolean dataInitialized;
    public PaymentMethodConfig(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (dataInitialized) {
            return;
        }

        List<PaymentMethod> list = new ArrayList<>();
        list.add(new PaymentMethod(null, "Mercado Pago"));
        list.add(new PaymentMethod(null, "Transferencia"));
        repository.saveAll(list);

    }
}

