package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.CreatePostRequest;
import com.blogsphere.blogsphere.dto.PostResponse;
import com.blogsphere.blogsphere.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(
            @RequestParam String keyword,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                postService.searchPosts(keyword, email)
        );
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        PostResponse response =
                postService.createPost(request, email);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                postService.getAllPosts(page, size, email)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                postService.getPostById(id, email)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CreatePostRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        PostResponse response =
                postService.updatePost(
                        id,
                        request,
                        email
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        postService.deletePost(id, email);

        return ResponseEntity.ok(
                "Post deleted successfully"
        );
    }
}