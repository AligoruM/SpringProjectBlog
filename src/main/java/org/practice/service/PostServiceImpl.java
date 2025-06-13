package org.practice.service;

import lombok.RequiredArgsConstructor;
import org.practice.dto.PostDto;
import org.practice.mapper.PostMapper;
import org.practice.model.PostDao;
import org.practice.repository.PostRepository;
import org.practice.repository.TagRepository;
import org.practice.utils.TagNormalizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final StorageService storageService;
    private final CommentService commentService;
    private final PostMapper postMapper;

    @Override
    public Page<PostDto> findPaged(Pageable pageable) {
        Long totalSize = postRepository.count();
        if (totalSize == 0) {
            return new PageImpl<>(List.of(), pageable, totalSize);
        }

        List<PostDto> pagedPosts = postMapper
                .toPostDtoList(postRepository.getPagedPosts(pageable.getOffset(), pageable.getPageSize()));
        populateTags(pagedPosts);
        populateCommentsCount(pagedPosts);
        return new PageImpl<>(pagedPosts, pageable, totalSize);
    }

    @Override
    public Page<PostDto> findPagedByTag(Pageable pageable, String searchTag) {
        List<Long> postIdsByTag = tagRepository.getPostIdsByTag(searchTag);
        if (postIdsByTag.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<PostDto> pagedPosts = postMapper.toPostDtoList(postRepository
                .getPagedPostsByIds(postIdsByTag, pageable.getOffset(), pageable.getPageSize()));
        populateTags(pagedPosts);
        populateCommentsCount(pagedPosts);
        return new PageImpl<>(pagedPosts, pageable, postIdsByTag.size());
    }

    private void populateTags(List<PostDto> posts) {
        //I don't like it, here should be method that will fetch tags in batch
        for (PostDto post : posts) {
            List<String> tagsByPostId = tagRepository.getTagsByPostId(post.getId());
            post.setTags(new HashSet<>(tagsByPostId));
        }
    }

    private void populateCommentsCount(List<PostDto> posts) {
        //same as for tags
        for (PostDto post : posts) {
            post.setCommentsCount(commentService.count(post.getId()));
        }
    }

    @Override
    public PostDto getById(Long id) {
        PostDto post = postMapper.toPostDto(postRepository.getById(id));
        List<String> tagsByPostId = tagRepository.getTagsByPostId(post.getId());
        post.setTags(new HashSet<>(tagsByPostId));
        post.setComments(commentService.findByPostId(id));
        return post;
    }

    @Override
    public PostDto save(PostDto post) {
        PostDao savedPost = postRepository.save(postMapper.toPostDao(post));
        Long id = savedPost.getId();
        tagRepository.saveTagsForPost(TagNormalizer.normalize(post.getRawTags()), id);
        if (post.getImage() != null) {
            storageService.store(post.getImage(), id);
        }
        post.setId(savedPost.getId());
        return post;
    }

    @Override
    public void delete(Long id) {
        storageService.delete(id.toString());
        postRepository.delete(id);
    }

    @Override
    public void update(PostDto post) {
        postRepository.update(postMapper.toPostDao(post));
        if (post.getImage() != null) {
            storageService.store(post.getImage(), post.getId());
        }
        tagRepository.updateTagsForPost(TagNormalizer.normalize(post.getRawTags()), post.getId());
    }

    /*I suppose it's should be better to load post, update likes and save itю
      In this implementation some business logic is in DAO lvl, that is not good,
      but I just left it as is, don't want to change.*/
    @Override
    public void changeLikesCount(Long id, Boolean like) {
        if (like) {
            postRepository.increaseLikesCount(id);
        } else {
            postRepository.decreaseLikesCount(id);
        }
    }
}
