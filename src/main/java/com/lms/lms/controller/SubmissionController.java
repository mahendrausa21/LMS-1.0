package com.lms.lms.controller;

import com.lms.lms.model.Assignment;
import com.lms.lms.model.Course;
import com.lms.lms.model.Notification;
import com.lms.lms.model.Submission;
import com.lms.lms.model.User;
import com.lms.lms.service.AssignmentService;
import com.lms.lms.service.CourseService;
import com.lms.lms.service.SubmissionService;
import com.lms.lms.service.NotificationService;
import com.lms.lms.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


@Controller
public class SubmissionController {
    private final SubmissionService submissionService;
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final NotificationService notificationService;
    private final UserService userService;
    private static final String UPLOAD_DIR = "uploads/";

    public SubmissionController(SubmissionService submissionService, AssignmentService assignmentService,
                                CourseService courseService, NotificationService notificationService, UserService userService) {
        this.submissionService = submissionService;
        this.assignmentService = assignmentService;
        this.courseService = courseService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/submissions/upload")
    public String uploadSubmissionGet(@RequestParam Long assignmentId, RedirectAttributes redirectAttributes) {
        // GET requests to upload endpoint redirect to submissions page
        return "redirect:/submissions";
    }

    @PostMapping("/submissions/upload")
    public String uploadSubmission(@RequestParam Long assignmentId, @RequestParam MultipartFile file, 
                                   Authentication authentication, RedirectAttributes redirectAttributes) {
        return performUpload(assignmentId, file, authentication, redirectAttributes);
    }

    private String performUpload(Long assignmentId, MultipartFile file, Authentication authentication, 
                                 RedirectAttributes redirectAttributes) {
        try {
            // Get authenticated user (student)
            User student = userService.findByEmail(authentication.getName());
            if (student == null) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/submissions";
            }
            
            Assignment assignment = assignmentService.findById(assignmentId);
            if (assignment == null) {
                redirectAttributes.addFlashAttribute("error", "Assignment not found");
                return "redirect:/submissions";
            }
            
            if (assignment.getCourse() == null) {
                redirectAttributes.addFlashAttribute("error", "Course not found for this assignment");
                return "redirect:/submissions";
            }
            
            // Validate file
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
                return "redirect:/submissions";
            }
            
            if (file.getSize() > 10 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("error", "File size exceeds 10MB limit");
                return "redirect:/submissions";
            }
            
            // Create upload directory structure
            Path uploadPath = Paths.get(UPLOAD_DIR, "course_" + assignment.getCourse().getId(), 
                                       "assignment_" + assignmentId);
            Files.createDirectories(uploadPath);
            
            // Save file
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());
            
            // Create submission
            Submission submission = new Submission();
            submission.setFilePath(filePath.toString());
            submission.setAssignment(assignment);
            submission.setStudent(student);
            submission.setSubmittedDate(LocalDateTime.now());
            submissionService.saveSubmission(submission);
            
            // Send notification to teacher
            User teacher = assignment.getCourse().getTeacher();
            if (teacher != null) {
                Notification notification = new Notification();
                notification.setMessage("New submission received from " + student.getName() + " for assignment: " + assignment.getTitle());
                notification.setUser(teacher);
                notification.setReadStatus(false);
                notificationService.createNotification(notification);
            }
            
            redirectAttributes.addFlashAttribute("success", "✓ File uploaded successfully!");
            return "redirect:/submissions";
            
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "File upload failed: " + e.getMessage());
            return "redirect:/submissions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/submissions";
        }
    }

    @Transactional
    @GetMapping("/submissions/list")
    public String listSubmissions(@RequestParam Long assignmentId, Model model) {
        try {
            List<Submission> submissions = submissionService.findSubmissionsByAssignment(assignmentId);
            model.addAttribute("submissions", submissions != null ? submissions : java.util.Collections.emptyList());
        } catch (Exception e) {
            model.addAttribute("submissions", java.util.Collections.emptyList());
        }
        return "submissions";
    }
    
    @Transactional
    @GetMapping("/submissions")
    public String mySubmissions(Authentication authentication, Model model) {
        try {
            User student = userService.findByEmail(authentication.getName());
            if (student == null) {
                model.addAttribute("submissions", java.util.Collections.emptyList());
                model.addAttribute("assignments", java.util.Collections.emptyList());
            } else {
                List<Submission> submissions = submissionService.findSubmissionsByStudent(student);
                List<Assignment> assignments = assignmentService.getAllAssignments();
                model.addAttribute("submissions", submissions != null ? submissions : java.util.Collections.emptyList());
                model.addAttribute("assignments", assignments != null ? assignments : java.util.Collections.emptyList());
            }
        } catch (Exception e) {
            model.addAttribute("submissions", java.util.Collections.emptyList());
            model.addAttribute("assignments", java.util.Collections.emptyList());
        }
        return "submissions";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @Transactional
    @GetMapping("/teacher/submissions")
    public String teacherSubmissions(Authentication authentication, Model model) {
        try {
            User teacher = (User) authentication.getPrincipal();
            // Get all courses taught by this teacher
            List<Course> courses = courseService.findCoursesByTeacher(teacher);
            courses = courses != null ? courses : java.util.Collections.emptyList();
            
            // Collect all submissions for assignments in these courses
            List<Submission> allSubmissions = new java.util.ArrayList<>();
            for (Course course : courses) {
                if (course != null && course.getAssignments() != null) {
                    for (Assignment assignment : course.getAssignments()) {
                        if (assignment != null) {
                            List<Submission> submissions = submissionService.findSubmissionsByAssignment(assignment.getId());
                            if (submissions != null) {
                                allSubmissions.addAll(submissions);
                            }
                        }
                    }
                }
            }
            model.addAttribute("submissions", allSubmissions);
            model.addAttribute("courses", courses);
        } catch (Exception e) {
            model.addAttribute("submissions", java.util.Collections.emptyList());
            model.addAttribute("courses", java.util.Collections.emptyList());
        }
        return "teacher-submissions";
    }

    @GetMapping("/submissions/download/{id}")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> downloadSubmissionFile(@PathVariable Long id) throws java.net.MalformedURLException {
        Submission submission = submissionService.findById(id);
        if (submission == null || submission.getFilePath() == null) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }
        java.nio.file.Path file = java.nio.file.Paths.get(submission.getFilePath());
        org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toUri());
        String filename = file.getFileName() != null ? file.getFileName().toString() : "file";
        return org.springframework.http.ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/submissions/assignment/{id}")
    public String submissionForAssignment(@PathVariable Long id, Authentication authentication, Model model) {
        User student = userService.findByEmail(authentication.getName());
        java.util.List<Assignment> assignments = assignmentService.getAllAssignments();
        java.util.List<Submission> submissions = student == null ? java.util.Collections.emptyList() : submissionService.findSubmissionsByStudent(student);
        model.addAttribute("assignments", assignments != null ? assignments : java.util.Collections.emptyList());
        model.addAttribute("submissions", submissions != null ? submissions : java.util.Collections.emptyList());
        model.addAttribute("selectedAssignmentId", id);
        return "submissions";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/submissions/{id}/grade")
    public String gradeSubmission(@PathVariable Long id, Model model) {
        try {
            Submission submission = submissionService.findById(id);
            if (submission == null) {
                return "redirect:/teacher/submissions?error=Submission+not+found";
            }
            model.addAttribute("submission", submission);
            model.addAttribute("assignment", submission.getAssignment());
            model.addAttribute("student", submission.getStudent());
            return "grade-submission";
        } catch (Exception e) {
            return "redirect:/teacher/submissions?error=Error+loading+submission";
        }
    }
}