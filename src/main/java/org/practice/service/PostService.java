package org.practice.service;

import org.practice.model.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostDto> findPaged(Pageable pageable);
    Page<PostDto> findPagedByTag(Pageable pageable, String searchTag);
    PostDto getById(Long id);
    PostDto save(PostDto post);
    void delete(Long id);
    void update(PostDto post);
    void changeLikesCount(Long id, Boolean like);
}
