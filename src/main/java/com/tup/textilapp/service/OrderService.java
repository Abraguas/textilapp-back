package com.tup.textilapp.service;

import com.tup.textilapp.exception.custom.InsufficientStockException;
import com.tup.textilapp.model.dto.*;
import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderStateRepository orderStateRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;


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
    public Integer registerOrder(OrderDTO orderDTO, String token) {
        if (orderDTO.getDetails().isEmpty()) {
            throw new IllegalArgumentException("No details provided");
        }
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(() -> new EntityNotFoundException("User: '" + this.jwtService.extractUserName(token) + "' doesn't exist"));
        OrderState state = this.orderStateRepository.findByName("Pendiente");
        Order newOrder = new Order(
                null,
                user,
                new Date(),
                state,
                orderDTO.getObservations(),
                null
        );
        Order rOrder = this.orderRepository.save(newOrder);
        for (OrderDetailDTO d : orderDTO.getDetails()) {
            Product product = this.productRepository.findById(d.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Specified product doesn't exist"));
            if (d.getQuantity() > product.getStock()) {
                throw new InsufficientStockException("Not enough '" + product.getName() + "' in stock");
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
        return rOrder.getId();
    }

    public List<GetOrderDTO> getAll() {
        return this.orderRepository.findAll().stream().map((Order o) -> new GetOrderDTO(
                o.getId(),
                o.getUserEntity().getUsername(),
                o.getDate(),
                o.getState(),
                o.getObservations(),
                o.getDetails().stream().map( (OrderDetail d) -> new OrderDetailDTO(
                        d.getQuantity(),
                        d.getPricePerUnit(),
                        d.getProduct()
                )).toList()
                )).collect(toList());
    }
    public PaginatedResponseDTO getByUsernameAndPageAndSize(Integer pageNum, Integer size, String infix) {
        Pageable p = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<Order> page = this.orderRepository.findAllByUserEntity_UsernameContainingIgnoreCase(infix,p);
        return mapOrdersToPaginatedResponse(page);
    }
    public PaginatedResponseDTO getByPageAndSize(Integer pageNum, Integer size) {
        Pageable p = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<Order> page = this.orderRepository.findAll(p);
        return mapOrdersToPaginatedResponse(page);
    }
    public GetOrderDTO getById(Integer orderId) {
        Order o = this.orderRepository.findById(orderId)
                .orElseThrow(()->new EntityNotFoundException("Specified order doesn't exist"));
        return new GetOrderDTO(
                o.getId(),
                o.getUserEntity().getUsername(),
                o.getDate(),
                o.getState(),
                o.getObservations(),
                o.getDetails().stream().map( (OrderDetail d) -> new OrderDetailDTO(
                        d.getQuantity(),
                        d.getPricePerUnit(),
                        d.getProduct()
                )).toList()
        );
    }
    public List<GetOrderDTO> getAllByToken(String token) {
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(() -> new EntityNotFoundException("User: '" + this.jwtService.extractUserName(token) + "' doesn't exist"));

        return this.orderRepository.findAllByUserEntity(user).stream().map((Order o) -> new GetOrderDTO(
                o.getId(),
                o.getUserEntity().getUsername(),
                o.getDate(),
                o.getState(),
                o.getObservations(),
                o.getDetails().stream().map( (OrderDetail d) -> new OrderDetailDTO(
                        d.getQuantity(),
                        d.getPricePerUnit(),
                        d.getProduct()
                )).toList()
        )).collect(toList());
    }
    public PaginatedResponseDTO getAllByTokenAndPageAndSize(String token, Integer pageNum, Integer size) {
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(() -> new EntityNotFoundException("User: '" + this.jwtService.extractUserName(token) + "' doesn't exist"));
        Pageable p = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, "date"));
        return mapOrdersToPaginatedResponse(orderRepository.findAllByUserEntity(user, p));
    }
    public List<GetOrderDTO> getPending() {
        List<OrderState> lst = new ArrayList<>();
        OrderState pendingState = this.orderStateRepository.findByName("Pendiente");
        lst.add(pendingState);
        OrderState payedState = this.orderStateRepository.findByName("Cobrado");
        lst.add(payedState);
        return this.orderRepository.findAllByStateIn(lst).stream().map((Order o) -> new GetOrderDTO(
                o.getId(),
                o.getUserEntity().getUsername(),
                o.getDate(),
                o.getState(),
                o.getObservations(),
                o.getDetails().stream().map( (OrderDetail d) -> new OrderDetailDTO(
                        d.getQuantity(),
                        d.getPricePerUnit(),
                        d.getProduct()
                )).toList()
        )).collect(toList());
    }
    public PaginatedResponseDTO getPendingByPageAndSize(Integer pageNum, Integer size) {
        Pageable p = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, "date"));
        Page<Order> page = this.orderRepository.findAllByStateIn(getPendingStates(),p);
        return mapOrdersToPaginatedResponse(page);
    }
    public PaginatedResponseDTO getPendingByUsernameAndPageAndSize(Integer pageNum, Integer size, String infix) {
        Pageable p = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, "date"));
        Page<Order> page = this.orderRepository.findAllByStateInAndUserEntity_UsernameContainingIgnoreCase(getPendingStates(),infix, p);
        return mapOrdersToPaginatedResponse(page);
    }
    public List<OrderState> getPendingStates() {
        List<OrderState> lst = new ArrayList<>();
        OrderState pendingState = this.orderStateRepository.findByName("Pendiente");
        lst.add(pendingState);
        OrderState payedState = this.orderStateRepository.findByName("Cobrado");
        lst.add(payedState);
        return lst;
    }

    private PaginatedResponseDTO mapOrdersToPaginatedResponse(Page<Order> page) {
        return new PaginatedResponseDTO(
                page.getContent().stream().map((Order o) -> new GetOrderDTO(
                        o.getId(),
                        o.getUserEntity().getUsername(),
                        o.getDate(),
                        o.getState(),
                        o.getObservations(),
                        o.getDetails().stream().map( (OrderDetail d) -> new OrderDetailDTO(
                                d.getQuantity(),
                                d.getPricePerUnit(),
                                d.getProduct()
                        )).toList()
                )).toArray(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Transactional
    public void changeState(Integer orderId,OrderState s) {
        OrderState state = this.orderStateRepository.findById(s.getId())
                .orElseThrow(()-> new EntityNotFoundException("State with id: '" + s.getId() + "' doesn't exist"));
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: '" + orderId + "' doesn't exist"));
        if (state.getName().equals("Cancelado")) {
            throw new IllegalStateException("You cannot cancel an order using this endpoint");
        }
        if (state.equals(order.getState())) {
            throw new IllegalStateException("The order already has the specified state");
        }
        order.setState(state);

    }

    @Transactional
    public void cancelOrder(Integer orderId, String token) throws AccessDeniedException {
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(() -> new EntityNotFoundException("User: '" + this.jwtService.extractUserName(token) + "' doesn't exist"));
        OrderState state = this.orderStateRepository.findByName("Cancelado");
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: '" + orderId + "' doesn't exist"));
        if (!user.equals(order.getUserEntity()) && !user.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Users with a client role can only cancel their own orders");
        }
        if (!order.getState().getName().equals("Pendiente") && !user.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Users with a client role can only cancel pending orders");
        }
        if (state.equals(order.getState())) {
            throw new IllegalStateException("The order already has the specified state");
        }
        for (OrderDetail d : order.getDetails()) {
            d.getProduct().setStock(d.getProduct().getStock() + d.getQuantity());
        }
        order.setState(state);
    }
    public List<HighestSellingProductsDTO> getHighestSellingProducts(Date startDate, Date endDate) {
        if (startDate.compareTo(endDate) > 0) {
            throw new IllegalStateException("Start date cannot be more recent than end date");
        }
        if (startDate.compareTo(endDate) == 0) {
            throw new IllegalStateException("Start date cannot be the exact same as end date");
        }
        return this.orderDetailRepository.highestSellingProducts(startDate, endDate);
    }
}
