package com.etienne.AI_Assisted_learningcompanion.controller;

import com.etienne.AI_Assisted_learningcompanion.service.AIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/ai-assistant")
    public String aiPage() {
        return "ai-assistant";
    }

    @PostMapping("/ask-ai")
    public String askAI(
            @RequestParam String question,
            Model model
    ) {

        String response = aiService.generateResponse(question);

        model.addAttribute("question", question);
        model.addAttribute("response", response);

        return "ai-assistant";
    }
}