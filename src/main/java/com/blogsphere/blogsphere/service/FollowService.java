package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.ProfileResponse;
import com.blogsphere.blogsphere.entity.Follow;
import com.blogsphere.blogsphere.entity.Notification;
import com.blogsphere.blogsphere.entity.NotificationType;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.repository.FollowRepository;
import com.blogsphere.blogsphere.repository.NotificationRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public FollowService(
            FollowRepository followRepository,
            UserRepository userRepository,
            NotificationRepository notificationRepository) {

        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public ProfileResponse getProfile(
            Long userId,
            String currentUserEmail) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        long followerCount =
                followRepository.countByFollowingId(userId);

        long followingCount =
                followRepository.countByFollowerId(userId);

        boolean isFollowing = false;

        if (currentUserEmail != null) {

            User currentUser = userRepository
                    .findByEmail(currentUserEmail)
                    .orElse(null);

            if (currentUser != null) {
                isFollowing = followRepository
                        .findByFollowerIdAndFollowingId(
                                currentUser.getId(),
                                userId
                        )
                        .isPresent();
            }
        }

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImage(),
                user.getBio(),
                followerCount,
                followingCount,
                isFollowing
        );
    }

    public List<User> getFollowers(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return followRepository.findByFollowingId(userId)
                .stream()
                .map(Follow::getFollower)
                .toList();
    }

    public List<User> getFollowing(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return followRepository.findByFollowerId(userId)
                .stream()
                .map(Follow::getFollowing)
                .toList();
    }

    public String followUser(Long userId, String email) {

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        User following = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User to follow not found"));

        if (follower.getId().equals(following.getId())) {
            throw new RuntimeException(
                    "You cannot follow yourself");
        }

        if (followRepository
                .findByFollowerIdAndFollowingId(
                        follower.getId(),
                        following.getId()
                )
                .isPresent()) {

            throw new RuntimeException(
                    "You are already following this user");
        }

        Follow follow = new Follow();

        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);

        // Create follow notification
        Notification notification = new Notification();

        notification.setRecipient(following);
        notification.setSender(follower);
        notification.setType(NotificationType.FOLLOW);
        notification.setMessage(
                follower.getName() + " started following you"
        );
        notification.setReferenceId(follower.getId());
        notification.setRead(false);

        notificationRepository.save(notification);

        return "User followed successfully";
    }

    public String unfollowUser(Long userId, String email) {

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        User following = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User to unfollow not found"));

        Follow follow = followRepository
                .findByFollowerIdAndFollowingId(
                        follower.getId(),
                        following.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "You are not following this user"));

        followRepository.delete(follow);

        return "User unfollowed successfully";
    }

    public long getFollowerCount(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return followRepository.countByFollowingId(userId);
    }

    public long getFollowingCount(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return followRepository.countByFollowerId(userId);
    }

    public boolean isFollowing(
            Long userId,
            String email) {

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return followRepository
                .findByFollowerIdAndFollowingId(
                        follower.getId(),
                        userId
                )
                .isPresent();
    }
}