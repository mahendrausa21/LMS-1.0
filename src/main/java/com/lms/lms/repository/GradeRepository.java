package com.lms.lms.repository;

import com.lms.lms.model.Grade;
import com.lms.lms.model.Submission;  // Import for submission reference
import com.lms.lms.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Grade findBySubmission(Submission submission);  // Custom method to find grade for a specific submission
    Grade findBySubmissionId(Long submissionId);  // Find grade by submission ID
    List<Grade> findBySubmissionStudent(User student);  // Assuming you might want grades for a student
}