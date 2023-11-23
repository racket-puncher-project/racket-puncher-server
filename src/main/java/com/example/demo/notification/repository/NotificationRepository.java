package com.example.demo.notification.repository;

import com.example.demo.entity.Apply;
import com.example.demo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findBySiteUser_Id(Long userId);
    void deleteByIdAndSiteUser_Id(Long notificationId, Long userId);
}