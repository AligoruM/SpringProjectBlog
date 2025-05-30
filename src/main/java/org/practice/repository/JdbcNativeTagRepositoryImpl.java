package org.practice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class JdbcNativeTagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcNativeTagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countByTag(String tag) {
        return jdbcTemplate.queryForObject("select count(*) from tags where tag = ?", Long.class, tag);
    }

    @Override
    public List<String> getTagsByPostId(Long postId) {
        return jdbcTemplate.queryForList("select distinct tag from tags where post_id = ?", String.class, postId);
    }

    @Override
    public List<Long> getPostIdsByTag(String tag) {
        return jdbcTemplate.queryForList("select distinct post_id from tags where tag = ?", Long.class, tag);
    }

    @Override
    public void saveTagsForPost(List<String> tags, Long postId) {
        jdbcTemplate.batchUpdate("insert into tags(post_id, tag) values (?,?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, postId);
                ps.setString(2, tags.get(i));
            }

            @Override
            public int getBatchSize() {
                return tags.size();
            }
        });
    }

    @Override
    public void updateTagsForPost(List<String> newTags, Long postId) {
        jdbcTemplate.update("delete from tags where post_id = ?", postId);
        saveTagsForPost(newTags, postId);
    }
}
