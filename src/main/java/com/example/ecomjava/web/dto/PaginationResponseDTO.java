package com.example.ecomjava.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponseDTO<T>    {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
