package com.tup.textilapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDTO {
    private Object[] result;
    private int currentPage;
    private Long totalItems;
    private int totalPages;
}
