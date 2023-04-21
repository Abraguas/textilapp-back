package com.tup.textilapp.service;

import com.tup.textilapp.model.dto.OrderDTO;
import com.tup.textilapp.model.dto.OrderDetailDTO;
import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderStateRepository orderStateRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            UserRepository userRepository,
            OrderStateRepository orderStateRepository,
            ProductRepository productRepository,
            JwtService jwtService
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.orderStateRepository = orderStateRepository;
        this.productRepository = productRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public void registerOrder(OrderDTO orderDTO, String token) {
        if(orderDTO.getDetails().isEmpty()) {
            throw new IllegalArgumentException("No details provided");
        }
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(()-> new IllegalArgumentException("User: '"+ this.jwtService.extractUserName(token) +"' doesn't exist"));
        OrderState state = this.orderStateRepository.findByName("Pendiente");
        Order newOrder = new Order(
                null,
                user,
                new Date(),
                state,
                orderDTO.getObservations(),
                null
        );
        this.orderRepository.save(newOrder);
        for (OrderDetailDTO d : orderDTO.getDetails()) {
            Product product = this.productRepository.findById(d.getProduct().getId())
                    .orElseThrow(()->new IllegalArgumentException("Specified product doesn't exist"));
            if(d.getQuantity() > product.getStock()) {
                throw new IllegalStateException("Not enough '" +product.getName()+"' in stock");
            }
            product.setStock(product.getStock() - d.getQuantity());
            this.orderDetailRepository.save(
                new OrderDetail(
                        null,
                        d.getQuantity(),
                        d.getPricePerUnit(),
                        product,
                        newOrder
                )
            );

        }
    }
    public List<Order> getAll() {
        return this.orderRepository.findAll();
    }
}
