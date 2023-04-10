package com.tup.textilapp.model.dto;

import com.tup.textilapp.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {


    private UserEntity userEntity;

    private String observations;

    private List<OrderDetailDTO> details;
}