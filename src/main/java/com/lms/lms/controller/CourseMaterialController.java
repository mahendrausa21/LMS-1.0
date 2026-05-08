package com.lms.lms.controller;

import com.lms.lms.model.Course;
import com.lms.lms.model.CourseMaterial;
import com.lms.lms.service.CourseMaterialService;
import com.lms.lms.service.CourseService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/materials")
public class CourseMaterialController {
    private final CourseMaterialService courseMaterialService;
    private final CourseService courseService;

    public CourseMaterialController(CourseMaterialService courseMaterialService,CourseService courseService) {
        this.courseMaterialService = courseMaterialService;
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/upload")
    public String uploadMaterial(@RequestParam MultipartFile file, CourseMaterial material) throws IOException {
        // Save file logic
        String uploadDir = "uploads/";
        java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + file.getOriginalFilename());
        java.nio.file.Files.write(filePath, file.getBytes());  // Ensure you handle file writing
        material.setFilePath(filePath.toString());  // This should now work
        courseMaterialService.uploadMaterial(material);
        return "redirect:/courses";
    }

    @GetMapping("/list")
    public String listMaterials(@RequestParam Long courseId, Model model) {
        Course course = courseService.findById(courseId);  // Fetch Course object
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        model.addAttribute("course", course);
        model.addAttribute("materials", courseMaterialService.findMaterialsByCourse(course));
        return "view-course";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long id) throws MalformedURLException {
        CourseMaterial material = courseMaterialService.findById(id);
        if (material == null || material.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }
        Path file = Paths.get(material.getFilePath());
        Resource resource = new UrlResource(file.toUri());
        String filename = file.getFileName() != null ? file.getFileName().toString() : "material";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}