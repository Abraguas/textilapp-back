package com.tup.textilapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalEarningsPerMonthDTO {
    private Integer year;
    private Integer month;
    private BigDecimal totalEarnings;
}
