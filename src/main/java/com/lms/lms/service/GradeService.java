package com.lms.lms.service;

import com.lms.lms.model.Grade;
import com.lms.lms.model.User;
import com.lms.lms.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public Grade assignGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Grade findBySubmissionId(Long submissionId) {
        // Custom logic if needed; assuming you have a method in repository
        return gradeRepository.findById(submissionId).orElseThrow(() -> new RuntimeException("Grade not found"));
    }

    public Grade findGradeBySubmissionId(Long submissionId) {
        return gradeRepository.findBySubmissionId(submissionId);
    }

    public List<Grade> findGradesByStudent(User student) {
        return gradeRepository.findBySubmissionStudent(student);  // As per repository
    }

    public Grade findById(Long id) {
        return gradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Grade not found"));
    }

    @Transactional
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}