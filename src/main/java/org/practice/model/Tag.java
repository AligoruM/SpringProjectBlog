package org.practice.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Tag {
    private Long id;
    private String name;
    private Set<Post> posts = new HashSet<>();
}
