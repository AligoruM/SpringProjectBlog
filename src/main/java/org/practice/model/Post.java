package org.practice.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Post {
    private Long id;
    private String title;
    private String imageUrl;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private Set<Tag> tags = new HashSet<>();
    private List<Comment> comments = new ArrayList<>();
}
