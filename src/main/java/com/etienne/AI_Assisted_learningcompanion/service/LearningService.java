package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.*;
import com.etienne.AI_Assisted_learningcompanion.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LearningService {

    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AchievementService achievementService;

    public LearningService(
            LessonRepository lessonRepository,
            LessonProgressRepository lessonProgressRepository,
            EnrollmentRepository enrollmentRepository,
            AchievementService achievementService
    ) {
        this.lessonRepository = lessonRepository;
        this.lessonProgressRepository = lessonProgressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.achievementService = achievementService;
    }

    public void completeLesson(User student, Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson == null) {
            return;
        }

        LessonProgress existing =
                lessonProgressRepository.findByStudentAndLesson(student, lesson);

        if (existing == null) {
            LessonProgress progress = new LessonProgress();
            progress.setStudent(student);
            progress.setLesson(lesson);
            progress.setCompleted(true);
            lessonProgressRepository.save(progress);
        }

        updateCourseProgress(student, lesson.getCourse());
    }

    private void updateCourseProgress(User student, Course course) {

        List<Lesson> courseLessons =
                lessonRepository.findByCourseId(course.getId());

        if (courseLessons.isEmpty()) {
            return;
        }

        int completedCount = 0;

        for (Lesson lesson : courseLessons) {
            LessonProgress progress =
                    lessonProgressRepository.findByStudentAndLesson(student, lesson);

            if (progress != null && progress.isCompleted()) {
                completedCount++;
            }
        }

        double percentage =
                (completedCount * 100.0) / courseLessons.size();

        Enrollment enrollment =
                enrollmentRepository.findByStudentAndCourse(student, course);

        if (enrollment != null) {
            enrollment.setProgressPercentage(percentage);

            if (percentage >= 100) {

                enrollment.setCompleted(true);

                if (enrollment.getCompletedAt() == null) {
                    enrollment.setCompletedAt(LocalDateTime.now());
                }

                achievementService.awardBadge(
                        student,
                        "First Course Completed",
                        "Awarded for completing your first course."
                );

            } else {

                enrollment.setCompleted(false);
                enrollment.setCompletedAt(null);
            }

            enrollmentRepository.save(enrollment);
        }
    }

    public boolean isLessonCompleted(User student, Lesson lesson) {

        LessonProgress progress =
                lessonProgressRepository.findByStudentAndLesson(student, lesson);

        return progress != null && progress.isCompleted();
    }

    public Long completeLessonAndReturnCourseId(User student, Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);

        if (lesson == null) {
            return null;
        }

        completeLesson(student, lessonId);

        return lesson.getCourse().getId();
    }
}