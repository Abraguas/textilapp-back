package com.tup.textilapp.model.dto;

import com.tup.textilapp.model.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Integer id;
    private String username;
    private Date date;
    private String observations;
    private BigDecimal ammountCharged;
    private String transactionNumber;

    private Integer orderId;

    private PaymentMethod paymentMethod;
}
