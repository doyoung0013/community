package com.schemaguard.community.postlike.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLikeResponse {

    private Long postId;
    private long likeCount;
    private boolean likedByCurrentUser;
}