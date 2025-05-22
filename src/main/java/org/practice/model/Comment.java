package org.practice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Post post;
}
