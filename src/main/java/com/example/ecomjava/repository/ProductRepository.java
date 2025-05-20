package com.example.ecomjava.repository;

import com.example.ecomjava.entity.ProductEntity;
import com.example.ecomjava.web.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    Optional<ProductEntity> findByIgnoreCaseProductName(String productName);

    Optional<ProductEntity> findByProductId(Long productId);

    Page<ProductEntity> findAllByIsDeleted(Pageable pageable, int isDeleted);
}
