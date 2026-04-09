package com.schemaguard.community.comment.repository;

import com.schemaguard.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"author", "post"})
    List<Comment> findByPostIdOrderByIdAsc(Long postId);

    void deleteByPostId(Long postId);
}