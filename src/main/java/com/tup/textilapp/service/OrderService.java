package com.tup.textilapp.service;

import com.tup.textilapp.model.dto.OrderDTO;
import com.tup.textilapp.model.dto.OrderDetailDTO;
import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderStateRepository orderStateRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            UserRepository userRepository,
            OrderStateRepository orderStateRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.orderStateRepository = orderStateRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void registerOrder(OrderDTO orderDTO) {
        Optional<UserEntity> user = this.userRepository.findById(orderDTO.getUserEntity().getId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Specified user doesn't exist");
        }
        Optional<OrderState> state = this.orderStateRepository.findById(orderDTO.getState().getId());
        if (state.isEmpty()) {
            throw new IllegalArgumentException("Specified state doesn't exist");
        }
        Order newOrder = new Order(
                null,
                user.get(),
                new Date(),
                state.get(),
                orderDTO.getObservations(),
                null
        );
        this.orderRepository.save(newOrder);
        for (OrderDetailDTO d : orderDTO.getDetails()) {
            Optional<Product> product = this.productRepository.findById(d.getProduct().getId());
            if (product.isEmpty()) {
                throw new IllegalArgumentException("Specified product doesn't exist");
            }
            this.orderDetailRepository.save(
                new OrderDetail(
                        null,
                        d.getQuantity(),
                        d.getPricePerUnit(),
                        product.get(),
                        newOrder
                )
            );

        }
    }
}
