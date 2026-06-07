package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model
    ) {

        User user =
                (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "profile";
    }
}