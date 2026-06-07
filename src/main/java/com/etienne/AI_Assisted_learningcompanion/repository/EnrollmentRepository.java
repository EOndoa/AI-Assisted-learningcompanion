package com.etienne.AI_Assisted_learningcompanion.repository;

import com.etienne.AI_Assisted_learningcompanion.model.Course;
import com.etienne.AI_Assisted_learningcompanion.model.Enrollment;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentAndCourse(User student, Course course);

    Enrollment findByStudentAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User student);
    int countByStudentAndCompletedTrue(User student);
}