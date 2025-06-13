package org.practice.service;

import lombok.RequiredArgsConstructor;
import org.practice.dto.CommentDto;
import org.practice.mapper.CommentMapper;
import org.practice.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

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
