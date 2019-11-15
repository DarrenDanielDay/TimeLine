package cn.ecnuer996.timeline.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest
{
    @Autowired
    private WebApplicationContext webApplicationContext;


    private MockMvc mockMvc;
    private MockHttpSession mockHttpSession;

    @BeforeAll
    public void setupMockMvc()
    {
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

    @Test
    void refresh()
    {
    }
}
