package com.blogsphere.blogsphere.dto;

import com.blogsphere.blogsphere.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String profileImage;
    private final String bio;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.bio = user.getBio();
        this.createdAt = user.getCreatedAt();
    }
}