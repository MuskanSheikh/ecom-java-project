package com.example.ecomjava.repository;

import com.example.ecomjava.entity.UserToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.beans.Transient;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);

    Optional<UserToken> findByUsername(String username);

    @Transactional
    @Modifying
    void deleteByUsername(String username);
}
