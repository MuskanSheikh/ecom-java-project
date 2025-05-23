package com.example.ecomjava.repository;

import com.example.ecomjava.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
             and case when :catId != 0 then pe.category_id = :catId else 1=1 end
             and case when :search != '' then (lower(pe.product_name) ilike concat('%', lower(:search),'%')\s
             or lower(pe.description) ilike concat('%', lower(:search),'%'))else 1=1 end
                    order by pe.product_id desc
        """,
            nativeQuery = true)
    Page<ProductEntity> getAllByIsDeletedAndCategoryId(Pageable pageable, @Param("isDeleted") int i, @Param("catId") Long category,@Param("search") String search);
}
