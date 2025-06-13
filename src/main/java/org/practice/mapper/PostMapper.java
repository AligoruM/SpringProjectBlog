package org.practice.mapper;

import org.mapstruct.Mapper;
import org.practice.model.PostDao;
import org.practice.dto.PostDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toPostDto(PostDao post);

    PostDao toPostDao(PostDto post);

    List<PostDto> toPostDtoList(List<PostDao> posts);
}
