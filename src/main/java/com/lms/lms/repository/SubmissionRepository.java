package com.lms.lms.repository;

import com.lms.lms.model.Submission;
import com.lms.lms.model.Assignment;
import com.lms.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignment(Assignment assignment);  // Existing method
    List<Submission> findByStudent(User student);  // Existing method
    
    // New custom method
    List<Submission> findByAssignmentId(Long assignmentId);  // This will query by the ID of the assignment
}