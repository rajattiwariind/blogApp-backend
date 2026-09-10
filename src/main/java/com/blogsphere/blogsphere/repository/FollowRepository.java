package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(
            Long followerId,
            Long followingId
    );

    List<Follow> findByFollowerId(Long followerId);

    List<Follow> findByFollowingId(Long followingId);

    long countByFollowingId(Long followingId);

    long countByFollowerId(Long followerId);
}