package org.practice.controller;

import org.apache.commons.lang3.StringUtils;
import org.practice.model.Post;
import org.practice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
                        @RequestParam("pageSize") Optional<Integer> size,
                        @RequestParam("search") Optional<String> search) {
        PageRequest pageable = PageRequest.of(page.orElse(0), size.orElse(5));

        Page<Post> posts;
        if (search.isPresent() && StringUtils.isNotBlank(search.get())) {
            posts = postService.findPagedByTag(pageable, search.get());
        } else {
            posts = postService.findPaged(pageable);
        }
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping({"/add"})
    public String addPostPage() {
        return "add-post";
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/posts";
    }

    @GetMapping({"/{id}"})
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

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") Long id, @RequestParam("like") Boolean like) {
        postService.changeLikesCount(id, like);
        return "redirect:/posts/" + id;
    }
}
