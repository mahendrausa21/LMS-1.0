package com.lms.lms.controller;

import com.lms.lms.model.User;
import com.lms.lms.service.CourseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    private final CourseService courseService;

    public StudentController(CourseService courseService) {
        this.courseService = courseService;
    }

    
}
