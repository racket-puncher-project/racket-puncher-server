/*
package com.example.demo.siteuser.repository;

import com.example.demo.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {
    Optional<EmailToken> findByIdAndExpirationTimeAfter(String emailTokenId, LocalDateTime now);
}*/
