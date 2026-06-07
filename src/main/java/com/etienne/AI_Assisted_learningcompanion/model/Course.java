package com.etienne.AI_Assisted_learningcompanion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @Column(length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    public Course() {
    }

    public Course(String title, String description, Major major) {
        this.title = title;
        this.description = description;
        this.major = major;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Major getMajor() {
        return major;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMajor(Major major) {
        this.major = major;
    }
}