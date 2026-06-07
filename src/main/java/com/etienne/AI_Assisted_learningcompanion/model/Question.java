package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String questionText;

    @Column(length = 1000)
    private String optionA;

    @Column(length = 1000)
    private String optionB;

    @Column(length = 1000)
    private String optionC;

    @Column(length = 1000)
    private String optionD;

    private String correctAnswer;

    private String difficultyLevel;

    @Column(length = 2000)
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getExplanation() {
        return explanation;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}