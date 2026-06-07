package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.AIUsage;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.AIUsageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIUsageService {

    private final AIUsageRepository aiUsageRepository;

    public AIUsageService(AIUsageRepository aiUsageRepository) {
        this.aiUsageRepository = aiUsageRepository;
    }

    public void saveQuestion(User student, String question) {

        AIUsage usage = new AIUsage();
        usage.setStudent(student);
        usage.setQuestion(question);
        usage.setAskedAt(LocalDateTime.now());

        aiUsageRepository.save(usage);
    }

    public List<AIUsage> getAllUsage() {
        return aiUsageRepository.findAll();
    }
}