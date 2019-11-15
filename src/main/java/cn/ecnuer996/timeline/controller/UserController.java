package cn.ecnuer996.timeline.controller;

import cn.ecnuer996.timeline.bean.Post;
import cn.ecnuer996.timeline.bean.User;
import cn.ecnuer996.timeline.dao.PostDao;
import cn.ecnuer996.timeline.dao.PostImageDao;
import cn.ecnuer996.timeline.dao.UserDao;
import cn.ecnuer996.timeline.service.PostService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private PostService postService;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/more-posts", method = RequestMethod.GET)
    public JSONObject getMorePosts(@RequestParam(required = false) String time) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        List<Post> posts;
        if (time != null && time != "") {
            Date date;
            try {
                date = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
                response.put("msg", "时间参数格式非法");
                response.put("code", "40400");
                return response;
            }
            //查询此时间之前的至多5条POST
            posts = postDao.listNextFivePostsBeforeTime(date);
        } else {
            //初次加载，返回最新五条POST
            posts = postDao.listLatestFivePost();
        }
        for (int i = 0; i < posts.size(); ++i) {
            Post post = posts.get(i);
            items.add(generatePostItem(post));
        }
        response.put("posts", items);
        return response;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public JSONObject refresh(@RequestParam String time) {
        JSONObject response = new JSONObject();
        JSONArray items = new JSONArray();
        List<Post> posts;
        if(time != null && !time.equals("") ) {
            Date date;
            try {
                date = dateFormat.parse(time);
            } catch (ParseException e) {
                //时间格式不正确或time为空字符串，返回空数组
                e.printStackTrace();
                response.put("posts", items);
                return response;
            }
            posts = postService.generateNewPostsRandomly(date);
        }
        else{
            posts = postDao.listLatestFivePost();
        }
        for(Post post : posts) {
            items.add(generatePostItem(post));
        }
        response.put("posts",items);
        return response;
    }

//    @RequestMapping(value = "/test-generate", method = RequestMethod.GET)
//    public JSONObject testGeneratePosts(@RequestParam String time) {
//        JSONObject response=new JSONObject();
//        JSONArray items=new JSONArray();
//        Date date;
//        try {
//            date = dateFormat.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//        List<Post> posts=postService.generateNewPostsRandomly(date);
//        for (int i = 0; i < posts.size(); ++i) {
//            Post post = posts.get(i);
//            items.add(generatePostItem(post));
//        }
//        response.put("posts",items);
//        return response;
//    }

    public JSONObject generatePostItem(Post post){
        JSONObject item = new JSONObject();
        User user = userDao.selectUserById(post.getUserId());
        List<String> images = postImageDao.selectImagesByPostId(post.getId());
        item.put("nickname", user.getNickname());
        item.put("avatar", user.getAvatar());
        item.put("id", post.getId());
        item.put("content", post.getContent());
        item.put("postTime", dateFormat.format(post.getTime()));
        item.put("images", images);
        return item;
    }

//    @RequestMapping(value = "more-posts",method = RequestMethod.GET)
//    public JSONObject getMorePosts(@RequestParam(required = false) Integer id){
//        JSONObject response=new JSONObject();
//        JSONArray items=new JSONArray();
//        List<Post> posts;
//        if(id!=null){
//            posts=postDao.listNextFivePost(id);
//        }else{
//            posts=postDao.listLatestFivePost();
//        }
//        for(int i=0;i<posts.size();++i){
//            JSONObject item=new JSONObject();
//            Post post=posts.get(i);
//            User user=userDao.selectUserById(post.getUserId());
//            List<String> images=postImageDao.selectImagesByPostId(post.getId());
//            item.put("nickname",user.getNickname());
//            item.put("avatar",user.getAvatar());
//            item.put("id",post.getId());
//            item.put("content",post.getContent());
//            item.put("postTime",dateFormat.format(post.getTime()));
//            item.put("images",images);
//            items.add(item);
//        }
//        response.put("posts",items);
//        return response;
//    }
}
