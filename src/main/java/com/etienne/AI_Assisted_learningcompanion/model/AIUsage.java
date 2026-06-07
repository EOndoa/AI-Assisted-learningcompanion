package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_usage")
public class AIUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String question;

    private LocalDateTime askedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    public AIUsage() {
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public LocalDateTime getAskedAt() {
        return askedAt;
    }

    public User getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAskedAt(LocalDateTime askedAt) {
        this.askedAt = askedAt;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}