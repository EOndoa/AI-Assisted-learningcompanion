package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.QuizResult;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quiz")
    public String quizPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("questions", quizService.getAllQuestions());

        return "quiz";
    }

    @GetMapping("/lesson/{lessonId}/quiz")
    public String lessonQuiz(
            @PathVariable Long lessonId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("lessonId", lessonId);
        model.addAttribute("questions", quizService.getQuestionsByLesson(lessonId));

        return "lesson-quiz";
    }

    @PostMapping("/lesson/{lessonId}/quiz/submit")
    public String submitLessonQuiz(
            @PathVariable Long lessonId,
            @RequestParam Map<String, String> answers,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        QuizResult result = quizService.submitLessonQuiz(user, lessonId, answers);

        model.addAttribute("result", result);
        model.addAttribute("lessonId", lessonId);

        return "lesson-quiz-result";
    }

    @PostMapping("/submit-quiz")
    public String submitQuiz(
            @RequestParam Map<String, String> answers,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        QuizResult result = quizService.submitQuiz(user, answers);

        model.addAttribute("result", result);

        return "quiz-result";
    }

    @GetMapping("/quiz-history")
    public String quizHistory(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("results", quizService.getStudentResults(user));

        return "quiz-history";
    }
}