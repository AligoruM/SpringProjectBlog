package org.practice.service;

import org.practice.model.Post;
import org.practice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findPaged(Pageable pageable) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startItem = currentPage * pageSize;

        List<Post> pagedPosts = postRepository.getPagedPosts(startItem, pageSize);

        int totalSize = pagedPosts.size();

        int toIndex = Math.min(startItem + pageSize, pagedPosts.size());
        pagedPosts = pagedPosts.subList(startItem, toIndex);

        return new PageImpl<>(pagedPosts, pageable, totalSize);
    }

    @Override
    public Post getById(Long id) {
        return postRepository.getById(id);
    }

    @Override
    public Post save(Post post) {
        MultipartFile image = post.getImage();
        //delegate save to storage service
        return postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }
}
