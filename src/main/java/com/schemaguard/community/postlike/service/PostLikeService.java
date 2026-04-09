package com.schemaguard.community.postlike.service;

import com.schemaguard.community.post.entity.Post;
import com.schemaguard.community.post.repository.PostRepository;
import com.schemaguard.community.postlike.dto.PostLikeResponse;
import com.schemaguard.community.postlike.entity.PostLike;
import com.schemaguard.community.postlike.repository.PostLikeRepository;
import com.schemaguard.community.user.entity.User;
import com.schemaguard.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    public boolean isLikedByCurrentUser(Long postId, String loginUserEmail) {
        if (loginUserEmail == null) {
            return false;
        }

        User user = userService.getByEmail(loginUserEmail);
        return postLikeRepository.existsByPostIdAndUserId(postId, user.getId());
    }

    public PostLikeResponse getPostLikeInfo(Long postId, String loginUserEmail) {
        return PostLikeResponse.builder()
                .postId(postId)
                .likeCount(getLikeCount(postId))
                .likedByCurrentUser(isLikedByCurrentUser(postId, loginUserEmail))
                .build();
    }

    @Transactional
    public void toggleLike(Long postId, String loginUserEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        User user = userService.getByEmail(loginUserEmail);

        postLikeRepository.findByPostIdAndUserId(postId, user.getId())
                .ifPresentOrElse(
                        postLikeRepository::delete,
                        () -> postLikeRepository.save(
                                PostLike.builder()
                                        .post(post)
                                        .user(user)
                                        .createdAt(LocalDateTime.now())
                                        .build()
                        )
                );
    }

    @Transactional
    public void deleteAllByPostId(Long postId) {
        postLikeRepository.deleteByPostId(postId);
    }
}