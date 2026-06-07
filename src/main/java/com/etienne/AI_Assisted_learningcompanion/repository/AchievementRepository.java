package com.etienne.AI_Assisted_learningcompanion.repository;

import com.etienne.AI_Assisted_learningcompanion.model.Achievement;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findByStudent(User student);

    boolean existsByStudentAndBadgeName(User student, String badgeName);
    int countByStudent(User student);
}