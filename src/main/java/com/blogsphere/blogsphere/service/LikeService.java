package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.entity.Notification;
import com.blogsphere.blogsphere.entity.NotificationType;
import com.blogsphere.blogsphere.entity.Like;
import com.blogsphere.blogsphere.entity.Post;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.repository.LikeRepository;
import com.blogsphere.blogsphere.repository.NotificationRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public LikeService(
            LikeRepository likeRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            NotificationRepository notificationRepository) {

        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public String likePost(Long postId, String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (likeRepository
                .findByPostIdAndUserId(postId, user.getId())
                .isPresent()) {

            throw new RuntimeException("Post already liked");
        }

        Like like = new Like();

        like.setPost(post);
        like.setUser(user);

        likeRepository.save(like);

        // Don't notify the user if they like their own post
        if (!post.getAuthor().getId().equals(user.getId())) {

            Notification notification = new Notification();

            notification.setRecipient(post.getAuthor());
            notification.setSender(user);
            notification.setType(NotificationType.LIKE);
            notification.setMessage(
                    user.getName() + " liked your post"
            );
            notification.setReferenceId(post.getId());
            notification.setRead(false);

            notificationRepository.save(notification);
        }

        return "Post liked successfully";
    }

    public String unlikePost(Long postId, String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Like like = likeRepository
                .findByPostIdAndUserId(
                        post.getId(),
                        user.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Post is not liked"));

        likeRepository.delete(like);

        return "Post unliked successfully";
    }

    public long getLikeCount(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }

        return likeRepository.countByPostId(postId);
    }
}