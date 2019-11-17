package cn.ecnuer996.timeline.controller;


import cn.ecnuer996.timeline.bean.Post;
import cn.ecnuer996.timeline.bean.User;
import cn.ecnuer996.timeline.dao.PostDao;
import cn.ecnuer996.timeline.dao.PostImageDao;
import cn.ecnuer996.timeline.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private PostDao postDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private PostImageDao postImageDao;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        List<Post> posts = new ArrayList<>();
        List<String> images = new ArrayList<>();
        images.add("1");
        images.add("2");
        images.add("3");
        images.add("4");
        images.add("5");
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());
        when(postDao.listLatestFivePost()).thenReturn(posts);
        when(postDao.listNextFivePostsBeforeTime(isA(Date.class))).thenReturn(posts);
        when(postDao.listPostsAfterTime(isA(Date.class))).thenReturn(posts);
        when(userDao.selectUserById(isA(Integer.class))).thenReturn(new User());
        when(userDao.selectAvatarByNickname(isA(String.class))).thenReturn("2");
        when(postImageDao.selectImagesByPostId(isA(Integer.class))).thenReturn(images);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2019-11-15 16:29:31", "2019-11-14 23:59:59"})
    @NullSource
    @EmptySource
    void getMorePostsWhenNotError(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/more-posts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("time", time)
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isNotEmpty()
        );
    }

    @Test
    void getMorePostsWhenError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/more-posts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("time", "adfasd"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.code")
                        .value("40400")
                ).andExpect(MockMvcResultMatchers
                .jsonPath("$.msg")
                .value("时间参数格式非法")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"2019-11-15 12:33:44", "2019-11-09 12:33:44"})
    public void testRefreshNormally(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/refresh")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("time", time)
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isArray()
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isNotEmpty()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    @EmptySource
    public void testRefreshEmptyString(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/refresh")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("time", time)
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isArray()
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isNotEmpty()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123", "2019-11 12:33:44", "2019-11-34 12:44"})
    public void testRefreshInvalidDate(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/refresh")
                .param("time", time)
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isArray()
        ).andExpect(MockMvcResultMatchers
                .jsonPath("$.posts")
                .isEmpty()
        );
    }

    @NullSource
    @ParameterizedTest
    public void testRefreshNull(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/refresh")
                .param("time", time)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
