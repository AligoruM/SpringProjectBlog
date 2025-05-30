package org.practice.service;

import org.practice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<Post> findPaged(Pageable pageable);
    Post getById(Long id);
    Post save(Post post);
    void delete(Long id);
}
