package com.schemaguard.community.postlike.controller;

import com.schemaguard.community.common.util.SecurityUtils;
import com.schemaguard.community.postlike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/posts/{postId}/like")
    public String toggleLike(@PathVariable("postId") Long postId) {
        postLikeService.toggleLike(postId, SecurityUtils.getCurrentUserEmail());
        return "redirect:/posts/" + postId;
    }
}