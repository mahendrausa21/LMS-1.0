package com.lms.lms.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String duration;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Enrollment> enrollments;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Assignment> assignments = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CourseMaterial> materials = new ArrayList<>();
    
    @ManyToMany(mappedBy = "enrolledCourses", fetch = FetchType.EAGER)
    private List<User> students = new ArrayList<>();
    
    
    public List<User> getStudents() {
        return students;
    }
    public void setStudents(List<User> students) {
        this.students = students;
    }
    
    public List<Assignment> getAssignments() {
        return assignments;
    }
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }
    
    public List<CourseMaterial> getMaterials() {
        return materials;
    }
    public void setMaterials(List<CourseMaterial> materials) {
        this.materials = materials;
    }
    
    // Getters and Setters
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }
    
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}