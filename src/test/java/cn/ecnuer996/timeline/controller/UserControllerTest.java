package cn.ecnuer996.timeline.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2019-11-15 16:29:31"})
    @NullSource
    @EmptySource
    void getMorePostsWhenNotError(String time) throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/more-posts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("time", time))
                .andExpect(MockMvcResultMatchers.jsonPath("$..posts").isNotEmpty());
    }
    @Test
    void getMorePostsWhenError() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/more-posts").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).param("time","adfasd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..code").value("40400")).andExpect(MockMvcResultMatchers.jsonPath("$..msg").value("时间参数格式非法"));
    }



    @ValueSource(strings = {"2019-11-11 12:33:44", "2018-11-11 12:33:44"})
    public void testRefreshNormally(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/refresh")
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
    public void testRefreshEmptyString(String time) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/refresh")
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
    @ValueSource(strings = {"abc", "123", "2019-11 12:33:44"})
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
