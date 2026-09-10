package com.blogsphere.blogsphere.dto;

import com.blogsphere.blogsphere.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String imageUrl;

    private final Long authorId;
    private final String authorName;
    private final String authorEmail;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final long likeCount;
    private final boolean likedByMe;

    public PostResponse(
            Post post,
            long likeCount,
            boolean likedByMe) {

        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();

        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getName();
        this.authorEmail = post.getAuthor().getEmail();

        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        this.likeCount = likeCount;
        this.likedByMe = likedByMe;
    }
}