package cn.ecnuer996.timeline.dao;

import cn.ecnuer996.timeline.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private UserRowMapper rowMapper=new UserRowMapper();

    public User selectUserById(int id){
        String sql="select * from user_t where id=?;";
        User user=jdbcTemplate.queryForObject(sql,rowMapper,id);
        return user;
    }

    public String selectAvatarByNickname(String nickname){
        String sql="select * from user_t where nickname=?";
        List<User> user = jdbcTemplate.query(sql,rowMapper,nickname);
        return user.get(0).getAvatar();
    }
}

class UserRowMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user=new User();
        user.setId(resultSet.getInt("id"));
        user.setNickname(resultSet.getString("nickname"));
        user.setAvatar(resultSet.getString("avatar"));
        return user;
    }
}
