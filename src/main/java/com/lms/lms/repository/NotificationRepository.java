package com.lms.lms.repository;

import com.lms.lms.model.Notification;
import com.lms.lms.model.User;  // Import for user reference
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);  // Custom method to find notifications for a specific user
    List<Notification> findByUserAndReadStatus(User user, boolean readStatus);  // Custom method to find unread notifications
}