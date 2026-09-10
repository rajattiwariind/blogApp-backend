package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.PostResponse;
import com.blogsphere.blogsphere.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                feedService.getFeed(email, page, size)
        );
    }
}