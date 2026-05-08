package com.lms.lms.service;

import com.lms.lms.model.Course;
import com.lms.lms.model.Enrollment;
import com.lms.lms.model.User;
import com.lms.lms.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserService userService,
                             CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Transactional
    public void enrollUser(Long userId, Long courseId) {

        User student = userService.findById(userId);
        Course course = courseService.findById(courseId);

        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        // Initialize collections if null
        if (student.getEnrolledCourses() == null) {
            student.setEnrolledCourses(new java.util.ArrayList<>());
        }

        if (course.getStudents() == null) {
            course.setStudents(new java.util.ArrayList<>());
        }

        // Avoid duplicate enrollment
        if (!student.getEnrolledCourses().contains(course)) {

            student.getEnrolledCourses().add(course);
            course.getStudents().add(student);

            // Save relationship
            userService.updateUser(student);
            courseService.saveCourse(course);

            // Save enrollment record
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollmentRepository.save(enrollment);
        }
    }
}