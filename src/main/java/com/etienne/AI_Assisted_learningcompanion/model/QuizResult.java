package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_results")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalQuestions;

    private int correctAnswers;

    private double percentage;

    private boolean passed;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    public QuizResult() {
    }

    public Long getId() {
        return id;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isPassed() {
        return passed;
    }

    public User getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}