package com.lms.lms.repository;

import com.lms.lms.model.Course;
import com.lms.lms.model.User;  // Import for teacher reference
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(User teacher);  // Custom method to find courses by a specific teacher

	List<Course> findByStudentsContaining(User student);
}