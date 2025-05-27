package org.practice.controller;

import org.practice.model.Post;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    @GetMapping
    public String posts(Model model,
                        @RequestParam("pageNumber") Optional<Integer> page,
                        @RequestParam("pageSize") Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);

        List<Post> posts = Arrays.asList(new Post(1L, "test title", "test text", 10, Set.of("test1"), Collections.emptyList()),
                new Post(2L, "test title 2", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 2", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 2", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 2", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(2L, "test title 2", "test text 2", 10, Set.of("test2"), Collections.emptyList()),
                new Post(3L, "test title 3", "test text 3", 10, Set.of("test3"), Collections.emptyList()));

        PageImpl<Post> postsPage = new PageImpl<>(posts, PageRequest.of(currentPage, pageSize), posts.size());
        model.addAttribute("posts", postsPage);
        return "posts";
    }
}
