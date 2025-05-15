package com.example.ecomjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddToCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    @Column(name = "product_id" , columnDefinition = "BIGINT default 0 ")
    private Long productId;

    @Column(name = "user_id", columnDefinition = "BIGINT default 0 ")
    private Long userId;

    @Column(name = "quantity", columnDefinition = "BIGINT default 0 ")
    private Long quantity;

    @Column(name = "is_deleted")
    private int isDeleted;
}
