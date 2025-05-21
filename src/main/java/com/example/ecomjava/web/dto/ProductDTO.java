package com.example.ecomjava.web.dto;

import com.example.ecomjava.entity.ProductEntity;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ProductDTO {
    private Long productId;

    private String productName;

    private String description;

    private Long quantity;

    private int oldPrice;

    private int newPrice;

    private Long categoryId;

    private String fileName;

    private Long cartQty;


    public static ProductDTO getEntity(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productEntity, productDTO);
        return productDTO;
    }
}
