package com.lms.lms.controller;

import com.lms.lms.model.Grade;
import com.lms.lms.model.Submission;
import com.lms.lms.model.User;
import com.lms.lms.service.GradeService;
import com.lms.lms.service.SubmissionService;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grades")
public class GradeController {
    private final GradeService gradeService;
    private final SubmissionService submissionService;

    public GradeController(GradeService gradeService, SubmissionService submissionService) {
        this.gradeService = gradeService;
        this.submissionService = submissionService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/assign")
    public String assignGrade(@RequestParam Long submissionId, @RequestParam double gradeValue, 
                             @RequestParam String feedback, RedirectAttributes redirectAttributes) {
        try {
            Submission submission = submissionService.findById(submissionId);
            if (submission == null) {
                redirectAttributes.addFlashAttribute("error", "Submission not found");
                return "redirect:/teacher/submissions";
            }
            
            // Validate grade value (0-100)
            if (gradeValue < 0 || gradeValue > 100) {
                redirectAttributes.addFlashAttribute("error", "Grade must be between 0 and 100");
                return "redirect:/submissions/" + submissionId + "/grade";
            }
            
            // Check if grade already exists for this submission
            Grade existingGrade = gradeService.findGradeBySubmissionId(submissionId);
            if (existingGrade != null) {
                // Update existing grade
                existingGrade.setGradeValue(gradeValue);
                existingGrade.setFeedback(feedback);
                gradeService.assignGrade(existingGrade);
            } else {
                // Create new grade
                Grade grade = new Grade();
                grade.setSubmission(submission);
                grade.setGradeValue(gradeValue);
                grade.setFeedback(feedback);
                gradeService.assignGrade(grade);
            }
            
            redirectAttributes.addFlashAttribute("success", "Grade assigned successfully!");
            return "redirect:/teacher/submissions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error assigning grade: " + e.getMessage());
            return "redirect:/teacher/submissions";
        }
    }

    @GetMapping("/my-grades")
    public String viewGrades(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();  // Cast to User
        model.addAttribute("grades", gradeService.findGradesByStudent(user));
        return "grades";
    }
}