package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.EnrollmentService;
import com.etienne.AI_Assisted_learningcompanion.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EnrollmentService enrollmentService;
    private final QuizService quizService;

    public HomeController(
            EnrollmentService enrollmentService,
            QuizService quizService
    ) {
        this.enrollmentService = enrollmentService;
        this.quizService = quizService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("loggedUser", user);

        return "dashboard";
    }

    @GetMapping("/progress")
    public String progress(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("loggedUser", user);
        model.addAttribute("enrollments", enrollmentService.getStudentEnrollments(user));
        model.addAttribute("quizResults", quizService.getStudentResults(user));

        return "progress";
    }
}