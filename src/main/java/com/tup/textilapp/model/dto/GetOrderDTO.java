package com.tup.textilapp.model.dto;

import com.tup.textilapp.model.entity.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDTO {

    private Integer id;

    private String username;

    private Date date;

    private OrderState state;

    private String observations;

    private List<OrderDetailDTO> details;
}
