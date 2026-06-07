package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.dto.LeaderboardDTO;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.AchievementRepository;
import com.etienne.AI_Assisted_learningcompanion.repository.EnrollmentRepository;
import com.etienne.AI_Assisted_learningcompanion.repository.QuizResultRepository;
import com.etienne.AI_Assisted_learningcompanion.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LeaderboardService {

    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final QuizResultRepository quizResultRepository;
    private final EnrollmentRepository enrollmentRepository;

    public LeaderboardService(
            UserRepository userRepository,
            AchievementRepository achievementRepository,
            QuizResultRepository quizResultRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.quizResultRepository = quizResultRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<LeaderboardDTO> getLeaderboard() {

        return userRepository.findAll()
                .stream()
                .filter(user -> "STUDENT".equalsIgnoreCase(user.getRole()))
                .map(user -> new LeaderboardDTO(
                        user.getFullName(),
                        user.getEmail(),
                        achievementRepository.countByStudent(user),
                        quizResultRepository.countByStudent(user),
                        enrollmentRepository.countByStudentAndCompletedTrue(user)
                ))
                .sorted(Comparator.comparingInt(LeaderboardDTO::getScore).reversed())
                .toList();
    }
}