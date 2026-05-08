package com.lms.lms.service;

import com.lms.lms.model.Notification;
import com.lms.lms.model.User;
import com.lms.lms.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findNotificationsByUser(User user) {
        return notificationRepository.findByUser(user);
    }

    public List<Notification> findUnreadNotificationsByUser(User user) {
        return notificationRepository.findByUserAndReadStatus(user, false);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void markAsRead(Notification notification) {
        notification.setReadStatus(true);  // This should now work
        notificationRepository.save(notification);
    }
    
    
}