package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.Course;
import com.etienne.AI_Assisted_learningcompanion.model.Enrollment;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.EnrollmentRepository;
import com.etienne.AI_Assisted_learningcompanion.service.CourseService;
import com.etienne.AI_Assisted_learningcompanion.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CertificateVerificationController {

    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentRepository enrollmentRepository;

    public CertificateVerificationController(
            UserService userService,
            CourseService courseService,
            EnrollmentRepository enrollmentRepository
    ) {
        this.userService = userService;
        this.courseService = courseService;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/verify-certificate")
    public String verifyCertificatePage() {
        return "verify-certificate";
    }

    @PostMapping("/verify-certificate")
    public String verifyCertificate(
            @RequestParam String certificateNumber,
            Model model
    ) {
        try {
            String[] parts = certificateNumber.split("-");

            Long courseId = Long.parseLong(parts[1]);
            Long userId = Long.parseLong(parts[2]);

            Course course = courseService.getCourseById(courseId);
            User user = userService.getUserById(userId);

            if (course == null || user == null) {
                model.addAttribute("error", "Certificate not found.");
                return "verify-certificate";
            }

            Enrollment enrollment =
                    enrollmentRepository.findByStudentAndCourse(user, course);

            if (enrollment == null || !enrollment.isCompleted()) {
                model.addAttribute("error", "Certificate is not valid.");
                return "verify-certificate";
            }

            model.addAttribute("valid", true);
            model.addAttribute("student", user);
            model.addAttribute("course", course);
            model.addAttribute("certificateNumber", certificateNumber);
            model.addAttribute("completedAt", enrollment.getCompletedAt());

            return "verify-certificate";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid certificate number format.");
            return "verify-certificate";
        }
    }
}