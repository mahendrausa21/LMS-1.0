package com.lms.lms.repository;

import com.lms.lms.model.ForumPost;
import com.lms.lms.model.Course;  // Import for course reference
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    List<ForumPost> findByCourse(Course course);  // Custom method to find posts for a specific course
    List<ForumPost> findByParentPost(ForumPost parentPost);  // Custom method to find replies to a post
}