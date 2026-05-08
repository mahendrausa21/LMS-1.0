package com.lms.lms.controller;

import com.lms.lms.model.Course;
import com.lms.lms.model.ForumPost;
import com.lms.lms.model.User;
import com.lms.lms.service.CourseService;
import com.lms.lms.service.ForumPostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forum")
public class ForumController {
    private final ForumPostService forumPostService;
    private final CourseService courseService;

    public ForumController(ForumPostService forumPostService,CourseService courseService) {
        this.forumPostService = forumPostService;
        this.courseService = courseService;
    }

    @GetMapping("/course/{courseId}")
    public String viewForum(@PathVariable Long courseId, Model model) {
        try {
            Course course = courseService.findById(courseId);
            if (course != null) {
                model.addAttribute("posts", forumPostService.findPostsByCourse(course));
                model.addAttribute("courseId", courseId);
                model.addAttribute("course", course);
            } else {
                model.addAttribute("posts", java.util.Collections.emptyList());
                model.addAttribute("error", "Course not found");
            }
        } catch (Exception e) {
            model.addAttribute("posts", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading forum");
        }
        return "forum";
    }

    @PostMapping("/post")
    public String createPost(@RequestParam Long courseId, ForumPost post, Authentication authentication) {
        try {
            // Get the course
            Course course = courseService.findById(courseId);
            if (course == null) {
                return "redirect:/forum/course/" + courseId;
            }
            
            // Set course on post
            post.setCourse(course);
            
            // Set user from authentication
            if (authentication != null && authentication.isAuthenticated()) {
                User user = (User) authentication.getPrincipal();
                post.setUser(user);
            }
            
            // Set timestamp if not already set
            if (post.getTimestamp() == null) {
                post.setTimestamp(new java.util.Date());
            }
            
            forumPostService.createPost(post);
            return "redirect:/forum/course/" + courseId;
        } catch (Exception e) {
            return "redirect:/forum/course/" + courseId;
        }
    }
}