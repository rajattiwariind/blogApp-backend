package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.ProfileResponse;
import com.blogsphere.blogsphere.entity.User;

import java.util.List;

import com.blogsphere.blogsphere.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(
            @PathVariable Long userId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                followService.getProfile(userId, email)
        );
    }

    @PostMapping
    public ResponseEntity<String> followUser(
            @PathVariable Long userId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                followService.followUser(userId, email)
        );
    }

    @DeleteMapping
    public ResponseEntity<String> unfollowUser(
            @PathVariable Long userId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                followService.unfollowUser(userId, email)
        );
    }

    @GetMapping
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long userId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                followService.isFollowing(userId, email)
        );
    }

    @GetMapping("/followers")
    public ResponseEntity<List<User>> getFollowers(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                followService.getFollowers(userId)
        );
    }

    @GetMapping("/following")
    public ResponseEntity<List<User>> getFollowing(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                followService.getFollowing(userId)
        );
    }
}