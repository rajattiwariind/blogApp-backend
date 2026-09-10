
package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.NotificationResponse;
import com.blogsphere.blogsphere.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                notificationService.getNotifications(email)
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                notificationService.getUnreadCount(email)
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {

        String email = authentication.getName();

        notificationService.markAsRead(
                notificationId,
                email
        );

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }
}

