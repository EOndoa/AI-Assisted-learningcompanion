package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;

    private double progressPercentage;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Enrollment() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public boolean isCompleted() {
        return completed;
    }

    public User getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}