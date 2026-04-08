package com.schemaguard.community.home.controller;

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
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("isAuthenticated", SecurityUtils.isAuthenticated());

        if (SecurityUtils.isAuthenticated()) {
            User user = userService.getByEmail(SecurityUtils.getCurrentUserEmail());
            model.addAttribute("loginUser", UserResponse.from(user));
        }

        return "index";
    }
}