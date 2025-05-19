package com.example.ecomjava.service;

import com.example.ecomjava.entity.CategoryEntity;
import com.example.ecomjava.entity.ProductEntity;
import com.example.ecomjava.web.dto.AddToCartDTO;
import com.example.ecomjava.web.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductEntity createProduct(ProductDTO productDTO, MultipartFile imageFile);

    List<CategoryEntity> getCategoryList();

    List<ProductEntity> getProductList();

    Long addToCart(AddToCartDTO addToCartDTO);

    ProductDTO getByProductId(Long productId);

    Long getCountByProductIdAndUserId(Long productId, Long userId);

    Long getCountByUserId(Long userId);

    List<ProductDTO> getCartDetail(Long userId);

    int removeFromCart(Long userId, Long productId);

    boolean checkoutCart(Long productId, Long qty,Long userId,String type);
}
