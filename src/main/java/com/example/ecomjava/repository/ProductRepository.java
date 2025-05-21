package com.example.ecomjava.repository;

import com.example.ecomjava.entity.ProductEntity;
import com.example.ecomjava.web.dto.ProductDTO;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    Optional<ProductEntity> findByIgnoreCaseProductName(String productName);

    Optional<ProductEntity> findByProductId(Long productId);

    Page<ProductEntity> findAllByIsDeleted(Pageable pageable, int isDeleted);


    @Query(value = """
            select pe.* from product_entity pe 
             left join category_entity ce on ce.category_id = pe.category_id 
             where pe.is_deleted = :isDeleted  
             and pe.category_id = :catId
                    order by pe.product_id desc
        """,
            nativeQuery = true)
    Page<ProductEntity> findAllByIsDeletedAndCategoryIgnoreCase(Pageable pageable, @Param("isDeleted") int i, @Param("catId") Long category);
}
