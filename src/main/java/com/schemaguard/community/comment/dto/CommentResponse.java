package com.schemaguard.community.comment.dto;

import com.schemaguard.community.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdAt;
    private boolean owner;

    public static CommentResponse from(Comment comment, Long loginUserId) {
        boolean isOwner = loginUserId != null && loginUserId.equals(comment.getAuthor().getId());

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorNickname(comment.getAuthor().getNickname())
                .createdAt(comment.getCreatedAt())
                .owner(isOwner)
                .build();
    }
}