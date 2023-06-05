package com.tup.textilapp.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tup.textilapp.exception.custom.MPApiRuntimeException;
import com.tup.textilapp.model.dto.*;
import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final IJwtService jwtService;
    private final OrderStateRepository orderStateRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    @Value("${site.url}")
    private String siteUrl;

    public PaymentService(OrderRepository orderRepository, UserRepository userRepository, IJwtService jwtService,
                          OrderStateRepository orderStateRepository, PaymentRepository paymentRepository,
                          PaymentMethodRepository paymentMethodRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.orderStateRepository = orderStateRepository;
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }


    public Preference createByOrder(Integer orderId, String token){
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(() -> new EntityNotFoundException("User: '" + this.jwtService.extractUserName(token) + "' doesn't exist"));
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: '" + orderId + "' doesn't exist"));
        if (!user.equals(order.getUserEntity()) && !user.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Users with a client role can only pay their own orders");
        }
        return this.createPreference(order);
    }

    public Preference createPreference(Order order){
        PreferenceClient client = new PreferenceClient();
        List<PreferenceItemRequest> items = new ArrayList<>();
        for (OrderDetail d : order.getDetails()) {
            PreferenceItemRequest item =
                    PreferenceItemRequest.builder()
                            .id(d.getProduct().getId().toString())
                            .title(d.getProduct().getName())
                            .description(d.getProduct().getDescription())
                            .pictureUrl(d.getProduct().getImage())
                            .quantity(d.getQuantity())
                            .currencyId("ARS")
                            .unitPrice(d.getPricePerUnit())
                            .build();
            items.add(item);
        }
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(this.siteUrl + "pay-order")
                .pending(this.siteUrl + "pay-order")
                .failure(this.siteUrl + "pay-order")
                .build();
        List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder().id("ticket").build());

        PreferencePaymentMethodsRequest paymentMethods =
                PreferencePaymentMethodsRequest.builder()
                        .excludedPaymentTypes(excludedPaymentTypes)
                        .build();
        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .binaryMode(true)
                .backUrls(backUrls)
                .paymentMethods(paymentMethods)
                .statementDescriptor("TextilApp")
                .externalReference(order.getId().toString())
                .build();
        try {
            return client.create(request);
        } catch (MPApiException e) {
            throw new MPApiRuntimeException(e.getStatusCode(), e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException(e.getMessage());
        }


    }
    public List<TotalEarningsPerMonthDTO> getTotalEarningsPerMonth(Date startDate, Date endDate) {
        if (startDate.compareTo(endDate) > 0) {
            throw new IllegalStateException("Start date cannot be more recent than end date");
        }
        if (startDate.compareTo(endDate) == 0) {
            throw new IllegalStateException("Start date cannot be the exact same as end date");
        }
        return this.paymentRepository.getTotalEarningsPerMonth(startDate, endDate);
    }
    public List<PaymentDTO> getAll() {
        return this.paymentRepository.findAll().stream().map(p ->
            new PaymentDTO(
                    p.getId(),
                    p.getOrder().getUserEntity().getUsername(),
                    p.getDate(),
                    p.getObservations(),
                    p.getAmmountCharged(),
                    p.getTransactionNumber(),
                    p.getOrder().getId(),
                    p.getPaymentMethod()
            )
        ).toList();
    }
    public PaginatedResponseDTO getAllByPageAndSize(Integer pageNum, Integer pageSize) {
        Pageable p = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "date"));
        Page<PaymentEntity> page = this.paymentRepository.findAll(p);
        return mapPaymentsToPaginatedResponse(page);
    }
    public PaginatedResponseDTO getByUsernameAndPageAndSize(Integer pageNum, Integer size, String infix) {
        Pageable p = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<PaymentEntity> page = this.paymentRepository.findByOrder_UserEntity_UsernameContainingIgnoreCase(infix,p);
        return mapPaymentsToPaginatedResponse(page);
    }
    private PaginatedResponseDTO mapPaymentsToPaginatedResponse(Page<PaymentEntity> page) {
        return new PaginatedResponseDTO(
                page.getContent().stream().map(p ->
                        new PaymentDTO(
                                p.getId(),
                                p.getOrder().getUserEntity().getUsername(),
                                p.getDate(),
                                p.getObservations(),
                                p.getAmmountCharged(),
                                p.getTransactionNumber(),
                                p.getOrder().getId(),
                                p.getPaymentMethod()
                        )
                ).toArray(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Transactional
    public PaymentApprovedDTO validatePayment(Long paymentId) {
        PaymentClient client = new PaymentClient();
        Payment p;
        try {
            p = client.get(paymentId);
        } catch (MPApiException e) {
            throw new MPApiRuntimeException(e.getStatusCode(), e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (p.getStatus().equals("approved")) {
            Order order = this.orderRepository.findById(Integer.parseInt(p.getExternalReference()))
                    .orElseThrow(() -> new EntityNotFoundException("Order with id: '" + p.getExternalReference() + "' doesn't exist"));
            OrderState state = this.orderStateRepository.findByName("Cobrado");
            if (order.getState().equals(state)) {
                throw new IllegalStateException("The order was already payed");
            }
            order.setState(state);
            PaymentMethod pm = this.paymentMethodRepository.findByName("Mercado Pago");
            this.paymentRepository.save(new PaymentEntity(
                    null,
                    new Date(),
                    "Hecho a travéz de la página web",
                    p.getTransactionAmount(),
                    p.getId().toString(),
                    order,
                    pm
            ));
        }
        return new PaymentApprovedDTO(
                p.getStatus().equals("approved"),
                p.getStatus(),
                p.getStatusDetail()
        );
    }

    @Transactional
    public void registerPayment(RegisterPaymentDTO body) {

        Order order = this.orderRepository.findById(body.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order with id: '" + body.getOrderId() + "' doesn't exist"));
        OrderState state = this.orderStateRepository.findByName("Cobrado");
        if (order.getState().equals(state)) {
            throw new IllegalStateException("The order was already payed");
        }
        order.setState(state);
        PaymentMethod pm = this.paymentMethodRepository.findById(body.getPaymentMethod().getId())
                .orElseThrow(() -> new EntityNotFoundException("Provided payment method does not exist"));
        this.paymentRepository.save(new PaymentEntity(
                null,
                new Date(),
                body.getObservations(),
                order.getDetails().stream().map(detail -> BigDecimal.valueOf(detail.getQuantity()).multiply(detail.getPricePerUnit()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                body.getTransactionNumber(),
                order,
                pm
        ));

    }
}
