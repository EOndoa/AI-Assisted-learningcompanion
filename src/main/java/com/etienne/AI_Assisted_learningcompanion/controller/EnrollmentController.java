package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/courses/enroll/{courseId}")
    public String enrollCourse(
            @PathVariable Long courseId,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        enrollmentService.enrollStudent(user, courseId);

        return "redirect:/my-learning";
    }

    @GetMapping("/my-learning")
    public String myLearning(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "enrollments",
                enrollmentService.getStudentEnrollments(user)
        );

        return "my-learning";
    }
}