package org.practice.controller;

import org.practice.model.Post;
import org.practice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String posts(Model model,
                        @RequestParam("pageNumber") Optional<Integer> page,
                        @RequestParam("pageSize") Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        PageRequest pageable = PageRequest.of(currentPage, pageSize);

        Page<Post> posts = postService.findAll(pageable);

        model.addAttribute("posts", posts);
        return "posts";
    }
}
