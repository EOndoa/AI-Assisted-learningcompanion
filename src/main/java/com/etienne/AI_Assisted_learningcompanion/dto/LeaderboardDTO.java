package com.etienne.AI_Assisted_learningcompanion.dto;

public class LeaderboardDTO {

    private String fullName;
    private String email;
    private int badgeCount;
    private int quizCount;
    private int completedCourses;
    private int score;

    public LeaderboardDTO(String fullName, String email, int badgeCount, int quizCount, int completedCourses) {
        this.fullName = fullName;
        this.email = email;
        this.badgeCount = badgeCount;
        this.quizCount = quizCount;
        this.completedCourses = completedCourses;
        this.score = (badgeCount * 10) + (completedCourses * 20) + quizCount;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public int getQuizCount() {
        return quizCount;
    }

    public int getCompletedCourses() {
        return completedCourses;
    }

    public int getScore() {
        return score;
    }
}