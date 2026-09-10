package com.blogsphere.blogsphere.dto;

import lombok.Getter;

@Getter
public class ProfileResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String profileImage;
    private final String bio;

    private final long followerCount;
    private final long followingCount;
    private final boolean isFollowing;

    public ProfileResponse(
            Long id,
            String name,
            String email,
            String profileImage,
            String bio,
            long followerCount,
            long followingCount,
            boolean isFollowing) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.bio = bio;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.isFollowing = isFollowing;
    }
}