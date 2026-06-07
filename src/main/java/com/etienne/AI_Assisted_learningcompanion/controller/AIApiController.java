package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.service.AIService;
import com.etienne.AI_Assisted_learningcompanion.service.AIUsageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AIApiController {

    private final AIService aiService;
    private final AIUsageService aiUsageService;

    public AIApiController(
            AIService aiService,
            AIUsageService aiUsageService
    ) {
        this.aiService = aiService;
        this.aiUsageService = aiUsageService;
    }

    @PostMapping("/ask-ai")
    public Map<String, String> askAI(
            @RequestBody Map<String, String> request,
            HttpSession session
    ) {
        Map<String, String> result = new HashMap<>();

        try {
            User user = (User) session.getAttribute("loggedUser");

            String question = request.get("question");

            if (question == null || question.trim().isEmpty()) {
                result.put("response", "Please type a valid question.");
                return result;
            }

            String response = aiService.generateResponse(question);

            if (user != null) {
                aiUsageService.saveQuestion(user, question);
            }

            result.put("response", response);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.put("response", "AI error: " + e.getMessage());
            return result;
        }
    }
    @GetMapping("/lesson-summary/{lessonId}")
    public Map<String, String> lessonSummary(@PathVariable Long lessonId) {

        Map<String, String> result = new HashMap<>();

        String summary = aiService.summarizeLesson(lessonId);

        result.put("response", summary);

        return result;
    }
    @GetMapping("/env-test")
    public String envTest() {
        String key = System.getenv("GOOGLE_API_KEY");

        if (key == null || key.isBlank()) {
            return "GOOGLE_API_KEY is not found.";
        }

        return "GOOGLE_API_KEY is loaded. First characters: "
                + key.substring(0, Math.min(8, key.length()))
                + "********";
    }
}