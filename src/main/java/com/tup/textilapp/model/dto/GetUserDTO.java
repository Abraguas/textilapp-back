package com.tup.textilapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String lastname;
    private String phonenumber;
}
