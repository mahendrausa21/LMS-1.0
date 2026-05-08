package com.lms.lms.service;

import com.lms.lms.model.CourseMaterial;
import com.lms.lms.model.Course;
import com.lms.lms.repository.CourseMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CourseMaterialService {
    private final CourseMaterialRepository courseMaterialRepository;

    public CourseMaterialService(CourseMaterialRepository courseMaterialRepository) {
        this.courseMaterialRepository = courseMaterialRepository;
    }

    @Transactional
    public CourseMaterial uploadMaterial(CourseMaterial material) {
        return courseMaterialRepository.save(material);
    }

    public List<CourseMaterial> findMaterialsByCourse(Course course) {
        return courseMaterialRepository.findByCourse(course);
    }

    public CourseMaterial findById(Long id) {
        return courseMaterialRepository.findById(id).orElseThrow(() -> new RuntimeException("Course material not found"));
    }

    @Transactional
    public void deleteMaterial(Long id) {
        courseMaterialRepository.deleteById(id);
    }
}