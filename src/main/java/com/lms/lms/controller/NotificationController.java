package com.lms.lms.controller;

import com.lms.lms.model.Notification;
import com.lms.lms.model.User;  // Correct import for your entity
import com.lms.lms.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String viewNotifications(Model model, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();  // Correct cast
            if (user != null) {
                List<Notification> notifications = notificationService.findNotificationsByUser(user);
                model.addAttribute("notifications", notifications);
            } else {
                model.addAttribute("notifications", List.of());  // Empty list as fallback
            }
            return "notifications";
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching notifications: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/mark-read")
    public String markAsRead(@RequestParam Long id) {
        Notification notification = notificationService.findById(id);
        notificationService.markAsRead(notification);
        return "redirect:/notifications";
    }
}