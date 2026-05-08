package com.lms.lms.service;

import com.lms.lms.model.Submission;
import com.lms.lms.model.User;
import com.lms.lms.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private static final String UPLOAD_DIR = "uploads/";

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @PostConstruct
    public void initializeUploadDirectory() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Transactional
    public Submission saveSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public List<Submission> findSubmissionsByAssignment(Long assignmentId) {
        // Assuming you pass an Assignment object or ID; here, we'd need to fetch it first for exact match
        return submissionRepository.findByAssignmentId(assignmentId);  // Custom method if added
    }

    public List<Submission> findSubmissionsByStudent(User student) {
        return submissionRepository.findByStudent(student);
    }

    public Submission findById(Long id) {
        return submissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    @Transactional
    public void deleteSubmission(Long id) {
        submissionRepository.deleteById(id);
    }
}