package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String badgeName;

    private String description;

    private LocalDateTime awardedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    public Achievement() {
    }

    public Long getId() {
        return id;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getAwardedAt() {
        return awardedAt;
    }

    public User getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAwardedAt(LocalDateTime awardedAt) {
        this.awardedAt = awardedAt;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}