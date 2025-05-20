package com.example.ecomjava.repository;

import com.example.ecomjava.entity.AddToCart;
import jakarta.transaction.Transactional;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Registered
public interface AddToCartRepository extends JpaRepository<AddToCart, Integer> {
    @Query(value = "select coalesce(count(*), 0) from add_to_cart where user_id = :userId and lower(status) = lower(:status)", nativeQuery = true)
    Long countByUserIdAndStatus(@Param("userId") Long userId,@Param("status") String status);

    Optional<AddToCart> findByProductIdAndUserIdAndStatus(Long productId, Long userId,String status);

//    @Query(value = "select count(*) from add_to_cart atc where atc.product_id = :productId and user_id = :userId", nativeQuery = true)
//    Long getCountByProductIdAndUserId(@Param("productId")Long productId,@Param("userId") Long userId);

    List<AddToCart> findByUserIdAndStatus(Long userId,String status);

    @Modifying
    @Transactional
    int deleteByProductIdAndUserId(Long productId, Long userId);

}
