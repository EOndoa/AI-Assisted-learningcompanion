package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lesson_progress")
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public LessonProgress() {
    }

    public Long getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public User getStudent() {
        return student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}