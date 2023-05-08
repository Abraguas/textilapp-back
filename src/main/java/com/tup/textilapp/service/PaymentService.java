package com.tup.textilapp.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.tup.textilapp.model.dto.PaymentApprovedDTO;
import com.tup.textilapp.model.entity.*;
import com.tup.textilapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
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

    @Autowired
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


    public Preference createByOrder(Integer orderId, String token) throws AccessDeniedException, MPException, MPApiException  {
        UserEntity user = this.userRepository.findByUsername(this.jwtService.extractUserName(token))
                .orElseThrow(() -> new IllegalArgumentException("User: '" + this.jwtService.extractUserName(token) + "' doesn't exist"));
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id: '" + orderId + "' doesn't exist"));
        if (!user.equals(order.getUserEntity()) && !user.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Users with a client role can only pay their own orders");
        }
        return this.createPreference(order);
    }
    public Preference createPreference(Order order) throws MPException, MPApiException {
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
        return client.create(request);


    }
    @Transactional
    public PaymentApprovedDTO validatePayment(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        Payment p = client.get(paymentId);
        if (p.getStatus().equals("approved")) {
            Order order = this.orderRepository.findById(Integer.parseInt(p.getExternalReference()))
                    .orElseThrow(() -> new IllegalArgumentException("Order with id: '" + p.getExternalReference() + "' doesn't exist"));
            OrderState state =  this.orderStateRepository.findByName("Cobrado");
            if (order.getState().equals(state)) {
                throw new IllegalStateException("The order was already payed");
            }
            order.setState(state);
            PaymentMethod pm = this.paymentMethodRepository.findByName("Mercado Pago");
            this.paymentRepository.save( new PaymentEntity(
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
}
