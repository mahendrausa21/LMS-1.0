package com.lms.lms.service;

import com.lms.lms.model.ForumPost;
import com.lms.lms.model.Course;
import com.lms.lms.repository.ForumPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ForumPostService {
    private final ForumPostRepository forumPostRepository;

    public ForumPostService(ForumPostRepository forumPostRepository) {
        this.forumPostRepository = forumPostRepository;
    }

    @Transactional
    public ForumPost createPost(ForumPost post) {
        return forumPostRepository.save(post);
    }

    public List<ForumPost> findPostsByCourse(Course course) {
        return forumPostRepository.findByCourse(course);
    }

    public List<ForumPost> findRepliesToPost(ForumPost parentPost) {
        return forumPostRepository.findByParentPost(parentPost);
    }

    public ForumPost findById(Long id) {
        return forumPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Forum post not found"));
    }

    @Transactional
    public void deletePost(Long id) {
        forumPostRepository.deleteById(id);
    }
}