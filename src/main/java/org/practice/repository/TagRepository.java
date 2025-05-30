package org.practice.repository;

import java.util.List;

public interface TagRepository {
    List<String> getTagsByPostId(Long postId);
    List<Long> getPostIdsByTag(String tag);
    void saveTagsForPost(List<String> tags, Long postId);
    void updateTagsForPost(List<String> newTags, Long postId);
}
