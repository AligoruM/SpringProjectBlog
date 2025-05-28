package org.practice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String text;
    private int likesCount;
    private Set<String> tags = new HashSet<>();
    private String rawTags;
    private List<Comment> comments = new ArrayList<>();

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
