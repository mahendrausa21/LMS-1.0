package com.lms.lms.controller;

import com.lms.lms.model.Assignment;
import com.lms.lms.model.Course;
import com.lms.lms.model.User;
import com.lms.lms.model.Notification;
import com.lms.lms.service.AssignmentService;
import com.lms.lms.service.CourseService;
import com.lms.lms.service.NotificationService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final NotificationService notificationService;

    public AssignmentController(AssignmentService assignmentService, CourseService courseService,
                                NotificationService notificationService) {
        this.assignmentService = assignmentService;
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/create")
    public String createAssignmentForm(@RequestParam Long courseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.findById(courseId);
            if (course == null) {
                redirectAttributes.addFlashAttribute("error", "Course not found");
                return "redirect:/courses";
            }
            model.addAttribute("course", course);
            model.addAttribute("assignment", new Assignment());
            return "create-assignment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Course not found or error accessing course");
            return "redirect:/courses";
        }
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public String createAssignment(Assignment assignment, RedirectAttributes redirectAttributes) {
        try {
            assignmentService.createAssignment(assignment);
            
            // Send notification to all students enrolled in the course
            Course course = assignment.getCourse();
            if (course != null && course.getStudents() != null) {
                for (User student : course.getStudents()) {
                    Notification notification = new Notification();
                    notification.setMessage("New assignment created: " + assignment.getTitle() + " in course " + course.getTitle());
                    notification.setUser(student);
                    notification.setReadStatus(false);
                    notificationService.createNotification(notification);
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "✓ Assignment created and notifications sent!");
            return "redirect:/courses/" + course.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create assignment: " + e.getMessage());
            return "redirect:/courses";
        }
    }

    @Transactional
    @GetMapping
    public String listAssignments(@RequestParam(required = false) Long courseId, Model model) {
        try {
            if (courseId != null) {
                Course course = courseService.findById(courseId);
                if (course != null && course.getAssignments() != null) {
                    model.addAttribute("assignments", course.getAssignments());
                    model.addAttribute("course", course);
                } else {
                    model.addAttribute("assignments", java.util.Collections.emptyList());
                    model.addAttribute("course", course);
                }
            } else {
                List<Assignment> allAssignments = assignmentService.getAllAssignments();
                model.addAttribute("assignments", allAssignments != null ? allAssignments : java.util.Collections.emptyList());
                model.addAttribute("course", null);
            }
        } catch (Exception e) {
            model.addAttribute("assignments", java.util.Collections.emptyList());
            model.addAttribute("course", null);
        }
        return "assignments";
    }

    @Transactional
    @GetMapping("/{id}")
    public String viewAssignment(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Assignment assignment = assignmentService.findById(id);
            if (assignment == null) {
                redirectAttributes.addFlashAttribute("error", "Assignment not found");
                return "redirect:/assignments";
            }
            model.addAttribute("assignment", assignment);
            model.addAttribute("course", assignment.getCourse());
            return "assignment-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading assignment: " + e.getMessage());
            return "redirect:/assignments";
        }
    }
}