package org.practice.controller;

import org.apache.commons.lang3.StringUtils;
import org.practice.model.dto.CommentDto;
import org.practice.model.dto.PostDto;
import org.practice.service.CommentService;
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
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String posts(Model model,
                        @RequestParam("pageNumber") Optional<Integer> page,
                        @RequestParam("pageSize") Optional<Integer> size,
                        @RequestParam("search") Optional<String> search) {
        PageRequest pageable = PageRequest.of(page.orElse(0), size.orElse(5));

        Page<PostDto> posts;
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
    public String createPost(@ModelAttribute PostDto post) {
        PostDto savedPost = postService.save(post);
        return "redirect:/posts/" + savedPost.getId();
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute PostDto post) {
        postService.update(post);
        return "redirect:/posts/" + id;
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
    public String likePost(@PathVariable("id") Long id,
                           @RequestParam("like") Boolean like) {
        postService.changeLikesCount(id, like);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{postId}/comments")
    public String commentPost(@PathVariable("postId") Long postId,
                              @RequestParam("text") String comment) {
        commentService.save(new CommentDto(null, postId, comment));
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{commentId}")
    public String editComment(@PathVariable("postId") Long postId,
                              @PathVariable("commentId") Long commentId,
                              @RequestParam("text") String comment) {
        commentService.update(new CommentDto(commentId, postId, comment));
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return "redirect:/posts/" + postId;
    }
}
