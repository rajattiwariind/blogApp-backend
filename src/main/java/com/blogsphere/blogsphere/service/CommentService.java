package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.CommentResponse;
import com.blogsphere.blogsphere.dto.CreateCommentRequest;
import com.blogsphere.blogsphere.entity.Comment;
import com.blogsphere.blogsphere.entity.Notification;
import com.blogsphere.blogsphere.entity.NotificationType;
import com.blogsphere.blogsphere.entity.Post;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.repository.CommentRepository;
import com.blogsphere.blogsphere.repository.NotificationRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.blogsphere.blogsphere.exception.AccessDeniedException;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            NotificationRepository notificationRepository) {

        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public CommentResponse createComment(
            Long postId,
            CreateCommentRequest request,
            String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();

        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setAuthor(user);

        Comment savedComment =
                commentRepository.save(comment);

        // Create notification for post owner
        if (!post.getAuthor().getId().equals(user.getId())) {

            Notification notification = new Notification();

            notification.setRecipient(post.getAuthor());
            notification.setSender(user);
            notification.setType(NotificationType.COMMENT);
            notification.setMessage(
                    user.getName() + " commented on your post"
            );
            notification.setReferenceId(post.getId());
            notification.setRead(false);

            notificationRepository.save(notification);
        }

        return new CommentResponse(savedComment);
    }

    public List<CommentResponse> getComments(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }

        return commentRepository
                .findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentResponse::new)
                .toList();
    }

    public CommentResponse updateComment(
            Long commentId,
            CreateCommentRequest request,
            String email) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You are not allowed to edit this comment");

        }

        comment.setContent(request.getContent());

        Comment updatedComment =
                commentRepository.save(comment);

        return new CommentResponse(updatedComment);
    }

    public void deleteComment(
            Long commentId,
            String email) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment not found"));

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException(
                    "You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}