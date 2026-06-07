package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.Lesson;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.LearningService;
import com.etienne.AI_Assisted_learningcompanion.service.LessonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LearningController {

    private final LessonService lessonService;
    private final LearningService learningService;

    public LearningController(
            LessonService lessonService,
            LearningService learningService
    ) {
        this.lessonService = lessonService;
        this.learningService = learningService;
    }

    @GetMapping("/courses/{courseId}/lessons")
    public String courseLessons(
            @PathVariable Long courseId,
            @RequestParam(required = false) String completed,
            Model model,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        List<Lesson> lessons =
                lessonService.getLessonsByCourse(courseId);

        Map<Long, Boolean> completionMap =
                new HashMap<>();

        for (Lesson lesson : lessons) {

            completionMap.put(
                    lesson.getId(),
                    learningService.isLessonCompleted(
                            user,
                            lesson
                    )
            );
        }

        model.addAttribute("lessons", lessons);

        model.addAttribute(
                "completionMap",
                completionMap
        );

        if ("true".equals(completed)) {

            model.addAttribute(
                    "successMessage",
                    "Lesson marked as completed successfully."
            );
        }

        return "course-lessons";
    }

    @GetMapping("/lessons/complete/{lessonId}")
    public String completeLesson(
            @PathVariable Long lessonId,
            HttpSession session
    ) {

        User user =
                (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        Long courseId =
                learningService.completeLessonAndReturnCourseId(
                        user,
                        lessonId
                );

        if (courseId == null) {
            return "redirect:/my-learning";
        }

        return "redirect:/courses/" +
                courseId +
                "/lessons?completed=true";
    }
}