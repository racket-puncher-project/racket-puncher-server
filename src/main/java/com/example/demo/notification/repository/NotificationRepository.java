package com.example.demo.notification.repository;

import com.example.demo.entity.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<List<Notification>> findAllBySiteUser_Email(String email);
    void deleteByIdAndSiteUser_Id(Long notificationId, Long userId);
}