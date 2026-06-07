package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.Lesson;
import com.etienne.AI_Assisted_learningcompanion.service.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/lesson/{id}")
    public String lessonDetails(@PathVariable Long id, Model model) {

        Lesson currentLesson = lessonService.getLessonById(id);

        if (currentLesson == null) {
            return "redirect:/my-learning";
        }

        Long courseId = currentLesson.getCourse().getId();

        List<Lesson> lessons =
                lessonService.getLessonsByCourseOrdered(courseId);

        Lesson previousLesson = null;
        Lesson nextLesson = null;

        for (int i = 0; i < lessons.size(); i++) {

            if (lessons.get(i).getId().equals(id)) {

                if (i > 0) {
                    previousLesson = lessons.get(i - 1);
                }

                if (i < lessons.size() - 1) {
                    nextLesson = lessons.get(i + 1);
                }

                break;
            }
        }

        model.addAttribute("lesson", currentLesson);
        model.addAttribute("previousLesson", previousLesson);
        model.addAttribute("nextLesson", nextLesson);
        model.addAttribute("courseId", courseId);

        return "lesson-details";
    }
}