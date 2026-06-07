package com.etienne.AI_Assisted_learningcompanion.repository;

import com.etienne.AI_Assisted_learningcompanion.model.Lesson;
import com.etienne.AI_Assisted_learningcompanion.model.LessonProgress;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonProgressRepository
        extends JpaRepository<LessonProgress, Long> {

    LessonProgress findByStudentAndLesson(
            User student,
            Lesson lesson
    );

    List<LessonProgress> findByStudent(User student);
}