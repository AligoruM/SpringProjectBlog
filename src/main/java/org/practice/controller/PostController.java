package org.practice.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.practice.model.Post;
import org.practice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"/", ""})
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

    @PostMapping({"/", ""})
    public String posts(@ModelAttribute Post post) {
        postService.save(post);
        return "posts";
    }

    @GetMapping("/add")
    public String addPost() {
        return "add-post";
    }

    @GetMapping({"/{id}", "/{id}/"})
    public String posts(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", postService.getById(id));
        return "post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable("id") Long id, Model model) {
        model.addAttribute("post", postService.getById(id));
        return "add-post";
    }
}
