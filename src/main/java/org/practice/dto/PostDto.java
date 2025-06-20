package org.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String text;
    private int likesCount;
    private Set<String> tags = new HashSet<>();
    private String rawTags;
    private List<CommentDto> comments = new ArrayList<>();
    private int commentsCount;
    private MultipartFile image;

    public String getTextPreview() {
        if (text.length() > 200) {
            return text.substring(0, 200) + "...";
        }
        return text;
    }

    public String getTagsAsText(){
        return tags.stream().map(tag -> "#" + tag).collect(Collectors.joining(" "));
    }
}
