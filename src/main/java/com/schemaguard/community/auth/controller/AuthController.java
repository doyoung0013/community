package com.schemaguard.community.auth.controller;

import com.schemaguard.community.auth.dto.LoginRequest;
import com.schemaguard.community.auth.dto.SignUpRequest;
import com.schemaguard.community.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(
            @Valid @ModelAttribute("signUpRequest") SignUpRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userService.signUp(request);
        } catch (IllegalArgumentException e) {
            model.addAttribute("signUpError", e.getMessage());
            return "signup";
        }

        return "redirect:/login?signupSuccess=true";
    }
}