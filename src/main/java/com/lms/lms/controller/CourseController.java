package com.lms.lms.controller;

import com.lms.lms.model.Course;
import com.lms.lms.model.User;
import com.lms.lms.service.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Transactional
    @GetMapping
    public String listCourses(Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getRole() == User.Role.TEACHER) {
            List<Course> courses = courseService.findCoursesByTeacher(currentUser);
            model.addAttribute("courses", courses != null ? courses : java.util.Collections.emptyList());
        } else {
            List<Course> courses = courseService.findAllCourses();  // Students see all
            model.addAttribute("courses", courses != null ? courses : java.util.Collections.emptyList());
        }
        return "courses";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/create")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public String createCourse(Course course, Authentication authentication) {
        User teacher = (User) authentication.getPrincipal();
        course.setTeacher(teacher);  // This should now work
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @Transactional
    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        if (course == null) {
            return "redirect:/courses?error=Course+not+found";
        }
        model.addAttribute("course", course);
        return "view-course";
    }

}