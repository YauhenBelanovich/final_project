package com.gmail.yauhen2012.springbootmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void getReviewPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/reviews")
        ).andExpect(status().isOk())
                .andExpect(view()
                        .name("reviews"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void getDeleteReview_returnRedirect() throws Exception {
        this.mockMvc.perform(
                get("/reviews/1/delete")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews"));
    }

}