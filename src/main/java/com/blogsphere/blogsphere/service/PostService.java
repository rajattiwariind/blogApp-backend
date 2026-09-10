package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.CreatePostRequest;
import com.blogsphere.blogsphere.dto.PostResponse;
import com.blogsphere.blogsphere.entity.Post;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.repository.LikeRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.blogsphere.blogsphere.exception.AccessDeniedException;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            LikeRepository likeRepository) {

        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public List<PostResponse> searchPosts(
            String keyword,
            String email) {

        List<Post> posts =
                postRepository
                        .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                                keyword,
                                keyword
                        );

        return posts.stream()
                .map(post -> createPostResponse(post, email))
                .toList();
    }

    public PostResponse createPost(
            CreatePostRequest request,
            String email) {

        User author = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Post post = new Post();

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);

        return createPostResponse(savedPost, email);
    }

    public List<PostResponse> getAllPosts(
            int page,
            int size,
            String email) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Post> postPage =
                postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return postPage.getContent()
                .stream()
                .map(post -> createPostResponse(post, email))
                .toList();
    }

    public PostResponse getPostById(
            Long id,
            String email) {

        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        return createPostResponse(post, email);
    }

    public PostResponse updatePost(
            Long postId,
            CreatePostRequest request,
            String email) {

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        if (!existingPost.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You are not allowed to edit this post");
        }

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setImageUrl(request.getImageUrl());

        Post savedPost = postRepository.save(existingPost);

        return createPostResponse(savedPost, email);
    }

    public void deletePost(
            Long postId,
            String email) {

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        if (!existingPost.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You are not allowed to delete this post");
        }

        postRepository.delete(existingPost);
    }

    private PostResponse createPostResponse(
            Post post,
            String email) {

        long likeCount =
                likeRepository.countByPostId(post.getId());

        boolean likedByMe = false;

        if (email != null) {

            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user != null) {
                likedByMe =
                        likeRepository
                                .findByPostIdAndUserId(
                                        post.getId(),
                                        user.getId()
                                )
                                .isPresent();
            }
        }

        return new PostResponse(
                post,
                likeCount,
                likedByMe
        );
    }
}