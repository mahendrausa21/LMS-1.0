package com.lms.lms.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "forum_posts")
public class ForumPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date timestamp;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parent_post_id")  // For replies
    private ForumPost parentPost;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public ForumPost getParentPost() { return parentPost; }
    public void setParentPost(ForumPost parentPost) { this.parentPost = parentPost; }
}