package org.practice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDao {
    private Long id;
    private String title;
    private String text;
    private int likesCount;
}
