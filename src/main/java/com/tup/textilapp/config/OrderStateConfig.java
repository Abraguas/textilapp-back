package com.tup.textilapp.config;


import com.tup.textilapp.model.entity.OrderState;
import com.tup.textilapp.repository.OrderStateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderStateConfig implements CommandLineRunner {

    private final OrderStateRepository repository;
    @Value("${data.initialized}")
    private boolean dataInitialized;
    public OrderStateConfig(OrderStateRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (dataInitialized) {
            return;
        }

        List<OrderState> list = new ArrayList<>();
        list.add(new OrderState(null, "Pendiente"));
        list.add(new OrderState(null, "Cobrado"));
        list.add(new OrderState(null, "Entregado"));
        list.add(new OrderState(null, "Cancelado"));
        repository.saveAll(list);

    }
}
