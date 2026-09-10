package com.blogsphere.blogsphere.dto;

import com.blogsphere.blogsphere.entity.Notification;
import com.blogsphere.blogsphere.entity.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {

    private final Long id;
    private final Long senderId;
    private final String senderName;
    private final String senderProfileImage;

    private final NotificationType type;
    private final String message;
    private final Long referenceId;

    private final boolean read;
    private final LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {

        this.id = notification.getId();

        this.senderId = notification.getSender().getId();
        this.senderName = notification.getSender().getName();
        this.senderProfileImage =
                notification.getSender().getProfileImage();

        this.type = notification.getType();
        this.message = notification.getMessage();
        this.referenceId = notification.getReferenceId();

        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }
}