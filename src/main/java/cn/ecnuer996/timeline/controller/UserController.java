package cn.ecnuer996.timeline.controller;

import cn.ecnuer996.timeline.bean.Post;
import cn.ecnuer996.timeline.bean.User;
import cn.ecnuer996.timeline.dao.PostDao;
import cn.ecnuer996.timeline.dao.PostImageDao;
import cn.ecnuer996.timeline.dao.UserDao;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private PostImageDao postImageDao;

    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @RequestMapping(value="/all-user",method= RequestMethod.GET)
    public List<User> getAllUser(){
        return userDao.listAllUser();
    }

    @RequestMapping(value="/all-post",method= RequestMethod.GET)
    public List<Post> getAllPost(){
        return postDao.listAllPost();
    }

    @RequestMapping(value="/next-five-post",method= RequestMethod.GET)
    public List<Post> getNextFivePost(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        return postDao.listNextFivePost(id);
    }

    @RequestMapping(value = "/previous-five-post",method = RequestMethod.GET)
    public List<Post> getPreviousFivePost(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        return postDao.listPreviousFivePost(id);
    }

    @RequestMapping(value = "more-posts",method = RequestMethod.GET)
    public JSONObject getMorePosts(@RequestParam(required = false) Integer id){
        JSONObject response=new JSONObject();
        JSONArray items=new JSONArray();
        List<Post> posts;
        if(id!=null){
            posts=postDao.listNextFivePost(id);
        }else{
            posts=postDao.listLatestFivePost();
        }
        for(int i=0;i<posts.size();++i){
            JSONObject item=new JSONObject();
            Post post=posts.get(i);
            User user=userDao.selectUserById(post.getUserId());
            List<String> images=postImageDao.selectImagesByPostId(post.getId());
            item.put("nickname",user.getNickname());
            item.put("avatar",user.getAvatar());
            item.put("id",post.getId());
            item.put("content",post.getContent());
            item.put("postTime",dateFormat.format(post.getTime()));
            item.put("images",images);
            items.add(item);
        }
        response.put("posts",items);
        return response;
    }
}
