package cn.ecnuer996.timeline.dao;

import cn.ecnuer996.timeline.bean.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostImageDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
//    private PostImageRowMapper rowMapper=new PostImageRowMapper();

    public List<String> selectImagesByPostId(Integer postId){
        String sql="select url from post_image where post_id="+postId+";";
        return jdbcTemplate.queryForList(sql,String.class);
    }

    public void insert(PostImage postImage){
        String sql="insert into post_image values(?,?);";
        jdbcTemplate.update(sql,postImage.getPostId(),postImage.getUrl());
    }

}

//class PostImageRowMapper implements RowMapper<PostImage>{
//
//    @Override
//    public PostImage mapRow(ResultSet resultSet, int i) throws SQLException {
//        PostImage postImage=new PostImage();
//        postImage.setPostId(resultSet.getInt("post_id"));
//        postImage.setUrl(resultSet.getString("url"));
//        return postImage;
//    }
//}
