package com.schemaguard.community.user.controller;

import com.schemaguard.community.common.util.SecurityUtils;
import com.schemaguard.community.user.dto.UserResponse;
import com.schemaguard.community.user.entity.User;
import com.schemaguard.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public String myPage(Model model) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getByEmail(email);
        model.addAttribute("user", UserResponse.from(user));
        return "me";
    }
}