package com.example.demo.siteuser.repository;

import com.example.demo.entity.SiteUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByEmail(String email);
    Optional<SiteUser> findByPhoneNumber(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}