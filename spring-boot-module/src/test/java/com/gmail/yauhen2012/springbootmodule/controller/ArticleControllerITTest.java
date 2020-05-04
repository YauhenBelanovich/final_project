package com.gmail.yauhen2012.springbootmodule.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticleControllerITTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void getArticles_returnAllArticles() throws Exception {
        mvc.perform(
                get("/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void getArticlesNewPage_returnAllArticles() throws Exception {
        mvc.perform(
                get("/articles/new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void deleteArticle_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/articles"));
    }
}
