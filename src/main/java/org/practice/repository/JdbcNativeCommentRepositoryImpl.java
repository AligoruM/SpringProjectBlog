package org.practice.repository;

import org.practice.model.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcNativeCommentRepositoryImpl implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CommentDao> commentRowMapper = new RsToCommentMapper();

    @Autowired
    public JdbcNativeCommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer count(Long postId) {
        return jdbcTemplate.queryForObject("select count(*) from comments where post_id=?", Integer.class, postId);
    }

    @Override
    public List<CommentDao> findByPostId(Long postId) {
        return jdbcTemplate.query("select * from comments where post_id = ?", commentRowMapper, postId);
    }

    @Override
    public void save(CommentDao comment) {
        jdbcTemplate.update("insert into comments(post_id, text) values(?, ?)", comment.getPostId(), comment.getText());
    }

    @Override
    public void update(CommentDao comment) {
        jdbcTemplate.update("update comments set text = ? where id = ?", comment.getText(), comment.getId());
    }

    @Override
    public void delete(Long commentId) {
        jdbcTemplate.update("delete from comments where id=?", commentId);
    }

    private static class RsToCommentMapper implements RowMapper<CommentDao> {

        @Override
        public CommentDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentDao commentDao = new CommentDao();
            commentDao.setId(rs.getLong("id"));
            commentDao.setPostId(rs.getLong("post_id"));
            commentDao.setText(rs.getString("text"));
            return commentDao;
        }
    }
}
