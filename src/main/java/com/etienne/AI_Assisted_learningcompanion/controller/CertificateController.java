package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.Course;
import com.etienne.AI_Assisted_learningcompanion.model.Enrollment;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.EnrollmentRepository;
import com.etienne.AI_Assisted_learningcompanion.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.etienne.AI_Assisted_learningcompanion.service.CertificatePdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
public class CertificateController {

    private final CourseService courseService;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificatePdfService certificatePdfService;

    public CertificateController(
            CourseService courseService,
            EnrollmentRepository enrollmentRepository,
            CertificatePdfService certificatePdfService
    ) {
        this.courseService = courseService;
        this.enrollmentRepository = enrollmentRepository;
        this.certificatePdfService = certificatePdfService;
    }

    @GetMapping("/certificate/{courseId}")
    public String certificate(
            @PathVariable Long courseId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            return "redirect:/my-learning";
        }

        Enrollment enrollment =
                enrollmentRepository.findByStudentAndCourse(user, course);

        if (enrollment == null || !enrollment.isCompleted()) {
            return "redirect:/my-learning";
        }
        model.addAttribute("user", user);
        model.addAttribute("course", course);

        String certificateNumber =
                "AILC-" +
                        course.getId() +
                        "-" +
                        user.getId();

        model.addAttribute(
                "certificateNumber",
                certificateNumber
        );

        model.addAttribute(
                "completionDate",
                enrollment.getCompletedAt()
        );
        return "certificate";
    }
    @GetMapping("/certificate/download/{courseId}")
    public ResponseEntity<byte[]> downloadCertificate(
            @PathVariable Long courseId,
            HttpSession session
    ) throws Exception {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, "/login")
                    .build();
        }

        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, "/my-learning")
                    .build();
        }

        Enrollment enrollment =
                enrollmentRepository.findByStudentAndCourse(user, course);

        if (enrollment == null || !enrollment.isCompleted()) {
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, "/my-learning")
                    .build();
        }

        String certificateNumber =
                "AILC-" + course.getId() + "-" + user.getId();

        String completionDate =
                enrollment.getCompletedAt().toString();

        byte[] pdf =
                certificatePdfService.generateCertificatePdf(
                        user.getFullName(),
                        course.getTitle(),
                        certificateNumber,
                        completionDate
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}