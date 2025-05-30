package org.practice.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class TagNormalizer {
    public static List<String> normalize(String rawTags) {
        if (StringUtils.isEmpty(rawTags)) {
            return List.of();
        }
        return Arrays.stream(rawTags.split("\\s+"))
                .map(String::trim)
                .map(s -> StringUtils.stripStart(s, "#"))
                .map(StringUtils::uncapitalize)
                .toList();
    }
}
