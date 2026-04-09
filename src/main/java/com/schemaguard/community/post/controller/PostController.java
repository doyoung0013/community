package com.schemaguard.community.post.controller;

import com.schemaguard.community.category.service.CategoryService;
import com.schemaguard.community.common.util.SecurityUtils;
import com.schemaguard.community.post.dto.PostCreateRequest;
import com.schemaguard.community.post.dto.PostResponse;
import com.schemaguard.community.post.dto.PostUpdateRequest;
import com.schemaguard.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String postList(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model
    ) {
        model.addAttribute("posts", postService.getPosts(categoryId, keyword));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isAuthenticated", SecurityUtils.isAuthenticated());

        if (SecurityUtils.isAuthenticated()) {
            model.addAttribute("loginUserEmail", SecurityUtils.getCurrentUserEmail());
        }

        return "index";
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable("id") Long id, Model model) {
        String loginUserEmail = SecurityUtils.isAuthenticated() ? SecurityUtils.getCurrentUserEmail() : null;

        PostResponse post = postService.getPost(id, loginUserEmail);
        model.addAttribute("post", post);
        model.addAttribute("isAuthenticated", SecurityUtils.isAuthenticated());

        return "detail";
    }

    @GetMapping("/posts/write")
    public String writePage(Model model) {
        model.addAttribute("postCreateRequest", new PostCreateRequest());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "write";
    }

    @PostMapping("/posts")
    public String createPost(
            @Valid @ModelAttribute("postCreateRequest") PostCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "write";
        }

        Long postId = postService.createPost(request, SecurityUtils.getCurrentUserEmail());
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        PostResponse post = postService.getPost(id, SecurityUtils.getCurrentUserEmail());

        if (!post.isOwner()) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        PostUpdateRequest request = new PostUpdateRequest();
        request.setCategoryId(post.getCategoryId());
        request.setTitle(post.getTitle());
        request.setContent(post.getContent());

        model.addAttribute("postId", id);
        model.addAttribute("postUpdateRequest", request);
        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("postUpdateRequest") PostUpdateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        PostResponse post = postService.getPost(id, SecurityUtils.getCurrentUserEmail());

        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", id);
            model.addAttribute("post", post);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit";
        }

        postService.updatePost(id, request, SecurityUtils.getCurrentUserEmail());
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id, SecurityUtils.getCurrentUserEmail());
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/like")
    public String likePost(@PathVariable("id") Long id) {
        postService.increaseLikeCount(id);
        return "redirect:/posts/" + id;
    }
}