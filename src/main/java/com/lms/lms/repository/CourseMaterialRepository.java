package com.lms.lms.repository;

import com.lms.lms.model.CourseMaterial;
import com.lms.lms.model.Course;  // Import for course reference
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    List<CourseMaterial> findByCourse(Course course);  // Custom method to find materials for a specific course
}