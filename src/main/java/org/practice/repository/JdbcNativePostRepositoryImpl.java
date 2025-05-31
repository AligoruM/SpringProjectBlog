package org.practice.repository;

import org.practice.model.PostDao;
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
    private final RowMapper<PostDao> postRowMapper = new RsToPostMapper();

    @Autowired
    public JdbcNativePostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long count() {
        return jdbcTemplate.queryForObject("select count(*) from posts", Long.class);
    }

    @Override
    public List<PostDao> getPagedPosts(Long offset, int limit) {
        return jdbcTemplate.query("select id, title, text, likes from posts offset ? fetch next ? rows only",
                postRowMapper, offset, limit);
    }

    @Override
    public List<PostDao> getPagedPostsByIds(List<Long> ids, Long offset, int limit) {
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
    public PostDao getById(Long id) {
        return jdbcTemplate.queryForObject("select * from posts where id = ?", postRowMapper, id);
    }

    @Override
    public PostDao save(PostDao post) {
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
    public void update(PostDao post) {
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

    private static class RsToPostMapper implements RowMapper<PostDao> {
        @Override
        public PostDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostDao post = new PostDao();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setText(rs.getString("text"));
            post.setLikesCount(rs.getInt("likes"));
            return post;
        }
    }
}
