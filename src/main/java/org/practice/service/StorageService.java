package org.practice.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    void store(MultipartFile file, Long postId);

    void delete(String filename);
}
