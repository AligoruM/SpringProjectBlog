package org.practice.repository;

import org.practice.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class JdbcNativePostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> postRowMapper = new RsToPostMapper();
    @Autowired
    public JdbcNativePostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> getPagedPosts(int offset, int limit) {
        return jdbcTemplate.query("select id, title, text, likes from posts", postRowMapper);
    }

    @Override
    public Post getById(Long id) {
        return jdbcTemplate.queryForObject("select * from posts where id = ?", postRowMapper, id);
    }

    @Override
    public Post save(Post post) {
        jdbcTemplate.update("insert into posts(title, text) values (?,?)", post.getTitle(), post.getText());
        return post;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    private static class RsToPostMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setText(rs.getString("text"));
            post.setComments(List.of());
            post.setTags(Set.of("test1"));
            post.setLikesCount(rs.getInt("likes"));
            return post;
        }
    }
}
