package com.lms.lms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;  // Path to uploaded file
    private LocalDateTime submittedDate;
    
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDateTime submittedDate) { this.submittedDate = submittedDate; }
    
    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }
    
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
}