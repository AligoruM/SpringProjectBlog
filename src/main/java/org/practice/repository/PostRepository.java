package org.practice.repository;

import org.practice.model.PostDao;

import java.util.List;

public interface PostRepository {
    Long count();

    List<PostDao> getPagedPosts(Long offset, int limit);

    List<PostDao> getPagedPostsByIds(List<Long> ids, Long offset, int limit);

    PostDao getById(Long id);

    PostDao save(PostDao post);
    void delete(Long id);

    void update(PostDao post);
    void increaseLikesCount(Long id);
    void decreaseLikesCount(Long id);
}
