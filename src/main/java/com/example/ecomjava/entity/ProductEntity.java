package com.example.ecomjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity", columnDefinition = "BIGINT default 0 ")
    private Long quantity;

    @Column(name = "old_price", columnDefinition = "BIGINT default 0 ")
    private int oldPrice;

    @Column(name = "new_price", columnDefinition = "BIGINT default 0 ")
    private int newPrice;

    @Column(name = "category_id", columnDefinition = "BIGINT default 0 ")
    private Long categoryId;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="file_name")
    private String fileName;

    @Column(name="is_deleted", columnDefinition = "INT default 0 ")
    private int isDeleted;
}
