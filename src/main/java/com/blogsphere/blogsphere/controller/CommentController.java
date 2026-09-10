package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.CommentResponse;
import com.blogsphere.blogsphere.dto.CreateCommentRequest;
import com.blogsphere.blogsphere.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }



    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                commentService.createComment(
                        postId,
                        request,
                        email
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                commentService.getComments(postId)
        );
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                commentService.updateComment(
                        commentId,
                        request,
                        email
                )
        );
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication) {

        String email = authentication.getName();

        commentService.deleteComment(
                commentId,
                email
        );

        return ResponseEntity.ok(
                "Comment deleted successfully"
        );
    }
}