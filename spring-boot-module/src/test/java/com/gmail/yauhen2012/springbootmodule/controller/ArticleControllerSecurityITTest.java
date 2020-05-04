package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticleControllerSecurityITTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getArticlesPageWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    public void getArticleWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getArticlesPage_returnOk() throws Exception {
        mvc.perform(
                get("/articles")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getArticlesPageLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getArticleLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void deleteArticleWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteArticleLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteItemLikeSaleUser_returnOk() throws Exception {
        when(articleService.deleteArticleById(TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/articles/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/articles"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteArticlesLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/articles/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void editArticleWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                post("/articles/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void editArticleLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                post("/articles/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void editArticlesLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                post("/articles/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }
}
