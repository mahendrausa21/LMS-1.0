package com.lms.lms.service;

import com.lms.lms.model.Assignment;
import com.lms.lms.model.Course;
import com.lms.lms.repository.AssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> findAssignmentsByCourse(Course course) {
        return assignmentRepository.findByCourse(course);
    }
    
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment findById(Long id) {
        return assignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    @Transactional
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}