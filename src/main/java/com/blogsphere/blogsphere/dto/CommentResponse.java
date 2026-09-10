package com.blogsphere.blogsphere.dto;

import com.blogsphere.blogsphere.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private final Long id;
    private final String content;
    private final Long postId;
    private final Long authorId;
    private final String authorName;
    private final String authorProfileImage;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.authorId = comment.getAuthor().getId();
        this.authorName = comment.getAuthor().getName();
        this.authorProfileImage = comment.getAuthor().getProfileImage();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}