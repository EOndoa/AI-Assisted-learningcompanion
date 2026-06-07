package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute User user,
            Model model
    ) {

        User savedUser = userService.registerUser(user);

        model.addAttribute("email", savedUser.getEmail());
        model.addAttribute(
                "message",
                "Registration successful. Your account is pending administrator verification."
        );

        return "verification-sent";
    }

    @GetMapping("/verify")
    public String verifyAccount(
            @RequestParam String token,
            Model model
    ) {

        User user = userService.verifyAccount(token);

        if (user == null) {
            model.addAttribute(
                    "message",
                    "Invalid or expired verification link."
            );

            return "verification-result";
        }

        model.addAttribute(
                "message",
                "Your account has been verified successfully. You can now login."
        );

        return "verification-result";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        User user = userService.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        if (!user.isEnabled()) {
            model.addAttribute(
                    "error",
                    "Your account is pending administrator verification. Please contact the system administrator."
            );
            return "login";
        }

        session.setAttribute("loggedUser", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin";
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}