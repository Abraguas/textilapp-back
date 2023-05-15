package com.tup.textilapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingDTO {
    private Integer id;
    private String username;
    private String name;
    private String lastname;
    private BigDecimal totalMoneySpent;

}
