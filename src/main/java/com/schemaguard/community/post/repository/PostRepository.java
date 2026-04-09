package com.schemaguard.community.post.repository;

import com.schemaguard.community.post.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findAll();

    @EntityGraph(attributePaths = {"author", "category"})
    Optional<Post> findWithAuthorAndCategoryById(Long id);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findByTitleContainingIgnoreCaseOrderByIdDesc(String keyword);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findByCategoryIdOrderByIdDesc(Long categoryId);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findByCategoryIdAndTitleContainingIgnoreCaseOrderByIdDesc(Long categoryId, String keyword);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Post> findAllByOrderByIdDesc();
}