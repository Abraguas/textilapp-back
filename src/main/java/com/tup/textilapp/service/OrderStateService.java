package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.OrderState;
import com.tup.textilapp.repository.OrderStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStateService {
    private final OrderStateRepository orderStateRepository;

    @Autowired
    public OrderStateService(OrderStateRepository orderStateRepository) {
        this.orderStateRepository = orderStateRepository;
    }

    public List<OrderState> getAll() {
        return this.orderStateRepository.findAll();
    }
}
