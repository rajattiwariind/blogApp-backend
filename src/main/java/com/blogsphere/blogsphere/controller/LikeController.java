package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/like")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<String> likePost(
            @PathVariable Long postId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                likeService.likePost(postId, email)
        );
    }

    @DeleteMapping
    public ResponseEntity<String> unlikePost(
            @PathVariable Long postId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                likeService.unlikePost(postId, email)
        );
    }

    @GetMapping
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                likeService.getLikeCount(postId)
        );
    }
}