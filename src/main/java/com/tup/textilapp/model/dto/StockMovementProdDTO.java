package com.tup.textilapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementProdDTO {
    private Integer id;
    private Integer quantity;
    private Integer priorStock;
    private Date date;
    private String observations;
    private String product;
    private String unit;
}
