package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.Course;
import com.etienne.AI_Assisted_learningcompanion.model.Enrollment;
import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.CourseRepository;
import com.etienne.AI_Assisted_learningcompanion.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public void enrollStudent(User student, Long courseId) {

        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return;
        }

        boolean alreadyEnrolled =
                enrollmentRepository.existsByStudentAndCourse(student, course);

        if (alreadyEnrolled) {
            return;
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setProgressPercentage(0);

        enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(User student) {
        return enrollmentRepository.findByStudent(student);
    }
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}