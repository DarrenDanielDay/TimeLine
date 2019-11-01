package cn.ecnuer996.timeline.dao;

import cn.ecnuer996.timeline.bean.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PostDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Post> listAllPost(){
        List<Post> posts=jdbcTemplate.query("select * from post",new PostRowMapper());
        return posts;
    }
}

class PostRowMapper implements RowMapper<Post>{

    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        Post post=new Post();
        post.setId(resultSet.getInt("id"));
        post.setUserId(resultSet.getInt("user_id"));
        post.setTime(resultSet.getTimestamp("time"));
        post.setContent(resultSet.getString("content"));
        return post;
    }
}