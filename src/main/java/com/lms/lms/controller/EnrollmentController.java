package com.lms.lms.controller;

import com.lms.lms.model.User;
import com.lms.lms.service.EnrollmentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    public String enrollInCourse(@RequestParam Long courseId, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User authUser = (User) authentication.getPrincipal();

        try {
            enrollmentService.enrollUser(authUser.getId(), courseId);
            return "redirect:/courses";
        } catch (Exception e) {
            return "redirect:/courses?error=Enrollment+failed";
        }
    }

    @GetMapping("/enroll")
    public String enrollInCourseGet(@RequestParam Long courseId, Authentication authentication) {
        return enrollInCourse(courseId, authentication);
    }
}