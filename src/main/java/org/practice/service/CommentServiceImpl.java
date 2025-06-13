package org.practice.service;

import org.practice.dto.CommentDto;
import org.practice.repository.CommentRepository;
import org.practice.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public int count(Long postId) {
        return commentRepository.count(postId);
    }

    @Override
    public List<CommentDto> findByPostId(Long postId) {
        return commentMapper.toCommentDtoList(commentRepository.findByPostId(postId));
    }

    @Override
    public void save(CommentDto comment) {
        commentRepository.save(commentMapper.toCommentDao(comment));
    }

    @Override
    public void update(CommentDto comment) {
        commentRepository.update(commentMapper.toCommentDao(comment));
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.delete(commentId);
    }
}
