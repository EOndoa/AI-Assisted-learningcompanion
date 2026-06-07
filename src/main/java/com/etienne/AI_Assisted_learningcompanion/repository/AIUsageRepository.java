package com.etienne.AI_Assisted_learningcompanion.repository;

import com.etienne.AI_Assisted_learningcompanion.model.AIUsage;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIUsageRepository extends JpaRepository<AIUsage, Long> {

    List<AIUsage> findByStudent(User student);
}