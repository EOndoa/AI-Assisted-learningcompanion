package com.etienne.AI_Assisted_learningcompanion.repository;

import com.etienne.AI_Assisted_learningcompanion.model.QuizResult;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository
        extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findByStudent(User student);
    int countByStudent(User student);
}