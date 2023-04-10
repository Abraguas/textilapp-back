package com.tup.textilapp.model.dto;


import com.tup.textilapp.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private Integer quantity;
    private BigDecimal pricePerUnit;
    private Product product;

}
