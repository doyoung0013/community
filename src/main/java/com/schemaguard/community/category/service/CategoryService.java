package com.schemaguard.community.category.service;

import com.schemaguard.community.category.entity.Category;
import com.schemaguard.community.category.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    @Transactional
    public void initCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }

        categoryRepository.save(Category.builder()
                .name("공지")
                .description("공지사항")
                .createdAt(LocalDateTime.now())
                .build());

        categoryRepository.save(Category.builder()
                .name("자유")
                .description("자유게시판")
                .createdAt(LocalDateTime.now())
                .build());

        categoryRepository.save(Category.builder()
                .name("질문")
                .description("질문게시판")
                .createdAt(LocalDateTime.now())
                .build());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByIdAsc();
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}