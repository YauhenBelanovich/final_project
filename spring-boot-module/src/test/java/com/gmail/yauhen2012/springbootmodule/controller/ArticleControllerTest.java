package com.gmail.yauhen2012.springbootmodule.controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.ArticleService;
import com.gmail.yauhen2012.service.CommentService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.ArticleDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ArticleController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class ArticleControllerTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private UserService userService;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    void getArticlesPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/articles")
        ).andExpect(status().isOk())
                .andExpect(view().name("articles"));
    }

    @Test
    @WithMockUser
    void getArticle_returnOk() throws Exception {
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setUserId(1L);
        comment.setText("test");
        comments.add(comment);
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setComments(comments);
        articleDTO.setUserId(1L);

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("test");
        userDTO.setLastName("test");
        userDTO.setPatronymic("test");

        when(articleService.findById(1L)).thenReturn(articleDTO);
        when(userService.findUserById(1L)).thenReturn(userDTO);
        this.mockMvc.perform(
                get("/articles/1")
                        .param("id", "1")
        ).andExpect(status().isOk()).andExpect(view().name("article"));
    }

    @Test
    @WithMockUser
    void getDeleteArticle_returnRedirect() throws Exception {
        when(articleService.deleteArticleById(TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/articles/1/delete")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles"));
    }

    @Test
    @WithMockUser
    void getArticlesNewPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/articles/new")
        ).andExpect(status().isOk())
                .andExpect(view().name("article_new"));
    }
}