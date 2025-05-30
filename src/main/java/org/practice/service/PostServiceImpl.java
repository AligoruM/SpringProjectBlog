package org.practice.service;

import org.practice.model.Post;
import org.practice.repository.PostRepository;
import org.practice.repository.TagRepository;
import org.practice.utils.TagNormalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Page<Post> findPaged(Pageable pageable) {
        Long totalSize = postRepository.count();
        if (totalSize == 0) {
            return new PageImpl<>(List.of(), pageable, totalSize);
        }

        List<Post> pagedPosts = postRepository.getPagedPosts(pageable.getOffset(), pageable.getPageSize());
        populateTags(pagedPosts);
        return new PageImpl<>(pagedPosts, pageable, totalSize);
    }

    @Override
    public Page<Post> findPagedByTag(Pageable pageable, String searchTag) {
        List<Long> postIdsByTag = tagRepository.getPostIdsByTag(searchTag);
        if (postIdsByTag.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<Post> pagedPosts = postRepository.getPagedPostsByIds(postIdsByTag, pageable.getOffset(), pageable.getPageSize());
        populateTags(pagedPosts);
        return new PageImpl<>(pagedPosts, pageable, postIdsByTag.size());
    }

    private void populateTags(List<Post> posts) {
        for (Post post : posts) {
            List<String> tagsByPostId = tagRepository.getTagsByPostId(post.getId());
            post.setTags(new HashSet<>(tagsByPostId));
        }
    }

    @Override
    public Post getById(Long id) {
        Post post = postRepository.getById(id);
        List<String> tagsByPostId = tagRepository.getTagsByPostId(post.getId());
        post.setTags(new HashSet<>(tagsByPostId));
        return post;
    }

    @Override
    public Post save(Post post) {
        MultipartFile image = post.getImage();
        //delegate save to storage service
        Post savedPost = postRepository.save(post);
        tagRepository.saveTagsForPost(TagNormalizer.normalize(savedPost.getRawTags()), savedPost.getId());
        return savedPost;
    }

    @Override
    public void delete(Long id) {
        //delete image
        postRepository.delete(id);
    }
}
