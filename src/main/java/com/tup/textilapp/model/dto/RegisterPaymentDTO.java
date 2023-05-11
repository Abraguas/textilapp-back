package com.tup.textilapp.model.dto;

import com.tup.textilapp.model.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPaymentDTO {
    private String observations;
    private Integer orderId;
    private PaymentMethod paymentMethod;
    private String transactionNumber;
}
