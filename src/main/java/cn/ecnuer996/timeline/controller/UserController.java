package cn.ecnuer996.timeline.controller;

import cn.ecnuer996.timeline.bean.Post;
import cn.ecnuer996.timeline.bean.User;
import cn.ecnuer996.timeline.dao.PostDao;
import cn.ecnuer996.timeline.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;

    @RequestMapping(value="/all-user",method= RequestMethod.GET)
    public List<User> getAllUser(){
        return userDao.listAllUser();
    }

    @RequestMapping(value="/all-post",method= RequestMethod.GET)
    public List<Post> getAllPost(){
        return postDao.listAllPost();
    }
}
