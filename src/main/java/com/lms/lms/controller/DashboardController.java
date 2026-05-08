package com.lms.lms.controller;

import com.lms.lms.model.Course;
import com.lms.lms.model.User;
import com.lms.lms.service.CourseService;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CourseService courseService;

    public DashboardController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Transactional
    @GetMapping("/student-dashboard")
    public String studentDashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("courses", courseService.findCoursesByStudent(user));
        return "student-dashboard";
    }

    @Transactional
    @GetMapping("/teacher-dashboard")
    public String teacherDashboard(Model model, Authentication authentication) {
        User teacher = (User) authentication.getPrincipal();
        List<Course> courses = courseService.findCoursesByTeacher(teacher);
        model.addAttribute("courses", courses);
        return "teacher-dashboard";
    }
    
}
