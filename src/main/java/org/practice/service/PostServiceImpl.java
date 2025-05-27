package org.practice.service;

import org.practice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    @Override
    public Page<Post> findAll(Pageable pageable) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startItem = currentPage  * pageSize;

        List<Post> posts = Arrays.asList(new Post(1L, "test title", "test text", 10, Set.of("test1"), Collections.emptyList()),
                new Post(2L, "test title 2", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 3", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 4", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 5", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 6", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(3L, "test title 7", "test text 3", 10, Set.of("test3"), Collections.emptyList()));
        int totalSize = posts.size();

        int toIndex = Math.min(startItem + pageSize, posts.size());
        posts = posts.subList(startItem, toIndex);

        return new PageImpl<>(posts, pageable, totalSize);
    }
}
