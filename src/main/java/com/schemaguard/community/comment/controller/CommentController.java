package com.schemaguard.community.comment.controller;

import com.schemaguard.community.comment.dto.CommentCreateRequest;
import com.schemaguard.community.comment.service.CommentService;
import com.schemaguard.community.common.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public String createComment(
            @PathVariable("postId") Long postId,
            @Valid @ModelAttribute("commentCreateRequest") CommentCreateRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postId;
        }

        commentService.createComment(postId, request, SecurityUtils.getCurrentUserEmail());
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(commentId, SecurityUtils.getCurrentUserEmail());
        return "redirect:/posts/" + postId;
    }
}