package org.practice.repository;

import org.practice.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
    public Long count() {
        return jdbcTemplate.queryForObject("select count(*) from posts", Long.class);
    }

    @Override
    public List<Post> getPagedPosts(Long offset, int limit) {
        return jdbcTemplate.query("select id, title, text, likes from posts offset ? fetch next ? rows only",
                postRowMapper, offset, limit);
    }

    @Override
    public List<Post> getPagedPostsByIds(List<Long> ids, Long offset, int limit) {
        String sql = "select id, title, text, likes from posts where id in (select * from table(x bigint = ?)) offset ? fetch next ? rows only";
        return jdbcTemplate.query(psc -> {
            PreparedStatement ps = psc.prepareStatement(sql);
            ps.setArray(1, psc.createArrayOf("bigint", ids.toArray()));
            ps.setLong(2, offset);
            ps.setInt(3, limit);
            return ps;
        }, postRowMapper);
    }

    @Override
    public Post getById(Long id) {
        return jdbcTemplate.queryForObject("select * from posts where id = ?", postRowMapper, id);
    }

    @Override
    public Post save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc -> {
            PreparedStatement ps = psc.prepareStatement("insert into posts(title, text) values (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            return ps;
        }, keyHolder);

        post.setId(keyHolder.getKeyAs(Long.class));
        return post;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update("update posts set title = ?, text = ? where id = ?",
                post.getTitle(), post.getText(), post.getId());
    }

    @Override
    public void increaseLikesCount(Long id) {
        jdbcTemplate.update("update posts set likes = likes + 1 where id = ?", id);
    }

    @Override
    public void decreaseLikesCount(Long id) {
        jdbcTemplate.update("update posts set likes = likes - 1 where id = ?", id);
    }

    private static class RsToPostMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setText(rs.getString("text"));
            post.setComments(List.of());
            post.setLikesCount(rs.getInt("likes"));
            return post;
        }
    }
}
