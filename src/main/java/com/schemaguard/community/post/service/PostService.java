package com.schemaguard.community.post.service;

import com.schemaguard.community.category.entity.Category;
import com.schemaguard.community.category.service.CategoryService;
import com.schemaguard.community.post.dto.PostCreateRequest;
import com.schemaguard.community.post.dto.PostResponse;
import com.schemaguard.community.post.dto.PostSummaryResponse;
import com.schemaguard.community.post.dto.PostUpdateRequest;
import com.schemaguard.community.post.entity.Post;
import com.schemaguard.community.post.repository.PostRepository;
import com.schemaguard.community.postlike.service.PostLikeService;
import com.schemaguard.community.comment.service.CommentService;
import com.schemaguard.community.user.entity.User;
import com.schemaguard.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    public List<PostSummaryResponse> getPosts(Long categoryId, String keyword) {
        List<Post> posts;

        boolean hasCategory = categoryId != null;
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (hasCategory && hasKeyword) {
            posts = postRepository.findByCategoryIdAndTitleContainingIgnoreCaseOrderByIdDesc(categoryId, keyword);
        } else if (hasCategory) {
            posts = postRepository.findByCategoryIdOrderByIdDesc(categoryId);
        } else if (hasKeyword) {
            posts = postRepository.findByTitleContainingIgnoreCaseOrderByIdDesc(keyword);
        } else {
            posts = postRepository.findAllByOrderByIdDesc();
        }

        return posts.stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .authorNickname(post.getAuthor().getNickname())
                        .categoryName(post.getCategory().getName())
                        .likeCount(postLikeService.getLikeCount(post.getId()))
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList();
    }

    public PostResponse getPost(Long postId, String loginUserEmail) {
        Post post = postRepository.findWithAuthorAndCategoryById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Long loginUserId = null;
        if (loginUserEmail != null) {
            loginUserId = userService.getByEmail(loginUserEmail).getId();
        }

        long likeCount = postLikeService.getLikeCount(postId);
        boolean likedByCurrentUser = postLikeService.isLikedByCurrentUser(postId, loginUserEmail);

        return PostResponse.from(post, loginUserId, likeCount, likedByCurrentUser);
    }

    @Transactional
    public Long createPost(PostCreateRequest request, String loginUserEmail) {
        User author = userService.getByEmail(loginUserEmail);
        Category category = categoryService.getById(request.getCategoryId());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post).getId();
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request, String loginUserEmail) {
        Post post = postRepository.findWithAuthorAndCategoryById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        validateOwner(post, loginUserEmail);

        Category category = categoryService.getById(request.getCategoryId());
        post.update(request.getTitle(), request.getContent(), category);
    }

    @Transactional
    public void deletePost(Long postId, String loginUserEmail) {
        Post post = postRepository.findWithAuthorAndCategoryById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        validateOwner(post, loginUserEmail);

        commentService.deleteAllByPostId(postId);
        postLikeService.deleteAllByPostId(postId);
        postRepository.delete(post);
    }

    private void validateOwner(Post post, String loginUserEmail) {
        User loginUser = userService.getByEmail(loginUserEmail);

        if (!post.getAuthor().getId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정 또는 삭제할 수 있습니다.");
        }
    }
}