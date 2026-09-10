package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorId(Long authorId);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleKeyword,
            String contentKeyword
    );
}