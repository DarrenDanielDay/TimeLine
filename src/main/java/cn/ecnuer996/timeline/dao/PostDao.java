package cn.ecnuer996.timeline.dao;

import cn.ecnuer996.timeline.bean.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class PostDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Post> listAllPost(){
        List<Post> posts=jdbcTemplate.query("select * from post;",new PostRowMapper());
        return posts;
    }

    public List<Post> listNextFivePost(int id){
        String sql = "select * from post where id > " + id + " limit 5;";
        List<Post> posts=jdbcTemplate.query(sql,new PostRowMapper());
        return posts;
    }

    public List<Post> listPreviousFivePost(int id) {
        String sql = "select * from post where id < " + id + " limit 5;";
        return jdbcTemplate.query(sql,new PostRowMapper());
    }

    public List<Post> listLatestFivePost() {
        String sql = "select * from post order by time desc limit 5;";
        List<Post> posts=jdbcTemplate.query(sql,new PostRowMapper());
        return posts;
    }

    public List<Post> listPostsAfterTime(Date time){
        String sql="select * from post where time > ? order by time desc limit 10;";
        List<Post> posts=jdbcTemplate.query(sql,new PostRowMapper(),time);
        return posts;
    }

    public int insert(Post post){
        String sql="insert into post values(?,?,?,?);";
        return jdbcTemplate.update(sql,post.getId(),post.getUserId(),post.getTime(),post.getContent());
    }

    public List<Post> listNextFivePostsBeforeTime(Date time){
        String sql="select * from post where time < ? order by time desc limit 5;";
        List<Post> posts=jdbcTemplate.query(sql,new PostRowMapper(),time);
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
