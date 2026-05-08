package com.lms.lms.service;

import com.lms.lms.model.Course;
import com.lms.lms.model.User;  // For teacher reference
import com.lms.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesByTeacher(User teacher) {
        return courseRepository.findByTeacher(teacher);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

	 public List<Course> findCoursesByStudent(User student) {
        return courseRepository.findByStudentsContaining(student);
    }

	 
}