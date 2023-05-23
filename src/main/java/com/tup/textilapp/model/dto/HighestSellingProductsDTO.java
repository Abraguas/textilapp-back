package com.tup.textilapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighestSellingProductsDTO {
    private Integer productId;
    private String productName;
    private Long quantitySold;
}
