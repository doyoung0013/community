package com.schemaguard.community.post.dto;

import com.schemaguard.community.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private Long authorId;
    private String categoryName;
    private Long categoryId;
    private long likeCount;
    private boolean likedByCurrentUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean owner;

    public static PostResponse from(Post post, Long loginUserId, long likeCount, boolean likedByCurrentUser) {
        boolean isOwner = loginUserId != null && loginUserId.equals(post.getAuthor().getId());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorNickname(post.getAuthor().getNickname())
                .authorId(post.getAuthor().getId())
                .categoryName(post.getCategory().getName())
                .categoryId(post.getCategory().getId())
                .likeCount(likeCount)
                .likedByCurrentUser(likedByCurrentUser)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .owner(isOwner)
                .build();
    }
}