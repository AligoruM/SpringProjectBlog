package org.practice.repository;

import org.practice.model.Post;

import java.util.List;

public interface PostRepository {
    Long count();
    List<Post> getPagedPosts(Long offset, int limit);
    List<Post> getPagedPostsByIds(List<Long> ids, Long offset, int limit);
    Post getById(Long id);
    Post save(Post post);
    void delete(Long id);
    void increaseLikesCount(Long id);
    void decreaseLikesCount(Long id);
}
