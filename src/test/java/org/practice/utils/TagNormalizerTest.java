package org.practice.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagNormalizerTest {

    @Test
    public void testTagNormalizer_validTag_shouldReturnSameString() {
        String input = "tag1";
        List<String> normalized = TagNormalizer.normalize(input);
        assertThat(normalized).containsOnly("tag1");
    }

    @Test
    public void testTagNormalizer_emptyTag_shouldReturnEmptyCollection() {
        String input = "";
        List<String> normalized = TagNormalizer.normalize(input);
        assertThat(normalized).hasSize(0);
    }

    @Test
    public void testTagNormalizer_2tags_shouldReturn2tags() {
        String input = "tag1 tag2";
        List<String> normalized = TagNormalizer.normalize(input);
        assertThat(normalized).containsOnly("tag1", "tag2");
    }

    @Test
    public void testTagNormalizer_capitalizedTag_shouldReturnUncapitalized() {
        String input = "Tag1";
        List<String> normalized = TagNormalizer.normalize(input);
        assertThat(normalized).hasSize(1);
        assertThat(normalized).containsOnly("tag1");
    }

    @Test
    public void testTagNormalizer_2sameTags_shouldReturn1tag() {
        String input = "tag1 tag1";
        List<String> normalized = TagNormalizer.normalize(input);
        assertThat(normalized).hasSize(1);
        assertThat(normalized).containsOnly("tag1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"tag1 tag2", "tag1\ttag2", "tag1\ntag2"})
    public void testTagNormalizer_spaceSymbolsString2Tags_shouldReturn2Tags(String input) {
        List<String> normalized = TagNormalizer.normalize(input);
        assertThat(normalized).hasSize(2);
        assertThat(normalized).containsOnly("tag1", "tag2");
    }
}