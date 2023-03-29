package com.tup.textilapp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bill_details")
public class BillDetail {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal pricePerUnit;
    private Integer quantity;
    @ManyToOne(targetEntity = Product.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(targetEntity = Bill.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Bill bill;

}
