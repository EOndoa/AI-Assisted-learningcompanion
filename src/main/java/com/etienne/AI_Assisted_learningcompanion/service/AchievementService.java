package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.Achievement;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.AchievementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    public void awardBadge(User student, String badgeName, String description) {

        boolean alreadyAwarded =
                achievementRepository.existsByStudentAndBadgeName(student, badgeName);

        if (alreadyAwarded) {
            return;
        }

        Achievement achievement = new Achievement();
        achievement.setStudent(student);
        achievement.setBadgeName(badgeName);
        achievement.setDescription(description);
        achievement.setAwardedAt(LocalDateTime.now());

        achievementRepository.save(achievement);
    }

    public List<Achievement> getStudentAchievements(User student) {
        return achievementRepository.findByStudent(student);
    }
}