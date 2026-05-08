package com.lms.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double gradeValue;  // e.g., 85.0
    private String feedback;
    
    @OneToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getGradeValue() { return gradeValue; }
    public void setGradeValue(double gradeValue) { this.gradeValue = gradeValue; }
    // ... other getters and setters
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
}