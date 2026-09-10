package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.PostResponse;
import com.blogsphere.blogsphere.entity.Post;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.repository.FollowRepository;
import com.blogsphere.blogsphere.repository.LikeRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FeedService {

    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public FeedService(
            FollowRepository followRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            LikeRepository likeRepository) {

        this.followRepository = followRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public List<PostResponse> getFeed(
            String email,
            int page,
            int size) {

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (page < 0) {
            throw new RuntimeException("Page cannot be negative");
        }

        if (size < 1 || size > 50) {
            throw new RuntimeException("Size must be between 1 and 50");
        }

        List<User> followingUsers =
                followRepository.findByFollowerId(currentUser.getId())
                        .stream()
                        .map(follow -> follow.getFollowing())
                        .toList();

        List<Post> allFeedPosts = new ArrayList<>();

        // Current user's posts
        allFeedPosts.addAll(
                postRepository.findByAuthorId(currentUser.getId())
        );

        // Posts from followed users
        for (User user : followingUsers) {
            allFeedPosts.addAll(
                    postRepository.findByAuthorId(user.getId())
            );
        }

        // Newest posts first
        allFeedPosts.sort(
                Comparator.comparing(
                        Post::getCreatedAt,
                        Comparator.reverseOrder()
                )
        );

        // Pagination
        int start = page * size;

        if (start >= allFeedPosts.size()) {
            return List.of();
        }

        int end = Math.min(
                start + size,
                allFeedPosts.size()
        );

        List<Post> feedPosts =
                allFeedPosts.subList(start, end);

        List<PostResponse> responses = new ArrayList<>();

        for (Post post : feedPosts) {

            long likeCount =
                    likeRepository.countByPostId(post.getId());

            boolean likedByMe =
                    likeRepository
                            .findByPostIdAndUserId(
                                    post.getId(),
                                    currentUser.getId()
                            )
                            .isPresent();

            responses.add(
                    new PostResponse(
                            post,
                            likeCount,
                            likedByMe
                    )
            );
        }

        return responses;
    }
}