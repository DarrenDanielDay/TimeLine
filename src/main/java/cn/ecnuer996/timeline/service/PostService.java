package cn.ecnuer996.timeline.service;

import cn.ecnuer996.timeline.bean.Post;
import cn.ecnuer996.timeline.bean.User;
import cn.ecnuer996.timeline.dao.PostDao;
import cn.ecnuer996.timeline.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("PostService")
public class PostService {

    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;

    //随机生成一些post并插入到数据库
    public List<Post> generateNewPostsRandomly(Date latestPostTime){
        Date now=new Date();
        Date temp=new Date(latestPostTime.getTime());
        Random rand=new Random();
        temp.setTime(temp.getTime()+(60+rand.nextInt(20))*60000L); //给时间加上60分钟左右的间隔
        while(temp.before(now)){
            //生成一条发布时间为temp的POST记录
            //随机生成的发布者ID
            int userId=rand.nextInt(7)+1000;
            int nextUserId=rand.nextInt(7)+1000;
            User poster=userDao.selectUserById(userId);
            User nextUser=userDao.selectUserById(nextUserId);
            Post newPost=new Post();
            newPost.setUserId(userId);
            newPost.setTime(temp);
            newPost.setContent("我"+poster.getNickname()+"就喜欢跟"+nextUser.getNickname()+"一起玩");
            //插入新数据
            postDao.insert(newPost);
            temp.setTime(temp.getTime()+(60+rand.nextInt(20))*60000L); //给时间加上60分钟左右的间隔
        }
        return postDao.listPostsAfterTime(latestPostTime);
    }
}
