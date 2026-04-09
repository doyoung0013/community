package com.schemaguard.community.post.dto;

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
    private long likeCount;
    private LocalDateTime createdAt;
}