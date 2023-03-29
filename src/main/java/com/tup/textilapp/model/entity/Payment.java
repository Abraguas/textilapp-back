package com.tup.textilapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue
    private Integer id;
    private Date date;
    private String observations;
    private BigDecimal ammountCharged;
    private Integer transactionNumber;
    @ManyToOne(targetEntity = Bill.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Bill bill;
    @ManyToOne(targetEntity = PaymentMethod.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;
}
