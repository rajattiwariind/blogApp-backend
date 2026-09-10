package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.NotificationResponse;
import com.blogsphere.blogsphere.entity.Notification;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.repository.NotificationRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }
    public void markAsRead(Long notificationId, String email) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not allowed to modify this notification"
            );
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getNotifications(
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(NotificationResponse::new)
                .toList();
    }

    public long getUnreadCount(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return notificationRepository
                .countByRecipientIdAndReadFalse(user.getId());
    }
}