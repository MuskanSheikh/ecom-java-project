package com.example.ecomjava.web.dto;

import lombok.Data;

@Data
public class AddToCartDTO {
    private int cartId;

    private Long productId;

    private Long userId;

    private Long quantity;

    private int isDeleted;
}
