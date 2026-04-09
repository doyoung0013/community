package com.schemaguard.community.post.dto;

import com.schemaguard.community.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSummaryResponse {

    private Long id;
    private String title;
    private String authorNickname;
    private String categoryName;
    private Integer likeCount;
    private LocalDateTime createdAt;

    public static PostSummaryResponse from(Post post) {
        return PostSummaryResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .authorNickname(post.getAuthor().getNickname())
                .categoryName(post.getCategory().getName())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}