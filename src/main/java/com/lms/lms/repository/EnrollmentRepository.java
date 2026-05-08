package com.lms.lms.repository;

import com.lms.lms.model.Enrollment;
import com.lms.lms.model.User;  // Import for student reference
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);  // Custom method to find enrollments by a specific student
    List<Enrollment> findByCourseId(Long courseId);  // Custom method to find enrollments for a specific course
}