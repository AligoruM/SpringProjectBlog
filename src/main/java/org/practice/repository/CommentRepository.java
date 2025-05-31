package org.practice.repository;

import org.practice.model.CommentDao;

import java.util.List;

public interface CommentRepository {
    Integer count(Long postId);

    List<CommentDao> findByPostId(Long postId);

    void save(CommentDao comment);

    void update(CommentDao comment);

    void delete(Long commentId);
}
