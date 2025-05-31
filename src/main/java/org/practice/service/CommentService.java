package org.practice.service;


import org.practice.model.dto.CommentDto;

import java.util.List;

public interface CommentService {
    int count(Long postId);

    List<CommentDto> findByPostId(Long postId);

    void save(CommentDto comment);

    void update(CommentDto comment);

    void delete(Long commentId);
}
