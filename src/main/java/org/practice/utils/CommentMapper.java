package org.practice.utils;

import org.mapstruct.Mapper;
import org.practice.model.CommentDao;
import org.practice.model.dto.CommentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toCommentDto(CommentDao comment);

    CommentDao toCommentDao(CommentDto comment);

    List<CommentDto> toCommentDtoList(List<CommentDao> comments);
}
