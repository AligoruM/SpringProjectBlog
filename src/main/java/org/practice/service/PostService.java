package org.practice.service;

import org.practice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<Post> findAll(Pageable pageable);
}
