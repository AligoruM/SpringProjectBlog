package org.practice.repository;

import org.practice.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> getPagedPosts(int offset, int limit);
    Post getById(Long id);
    Post save(Post post);
    void delete(Long id);
}
