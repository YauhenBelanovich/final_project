package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.UserService;
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

@WebMvcTest(controllers = ReviewController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser
    void getReviewPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/reviews")
        ).andExpect(status().isOk())
                .andExpect(view()
                        .name("reviews"));
    }

    @Test
    @WithMockUser
    void getDeleteReview_returnRedirect() throws Exception {
        when(reviewService.deleteReviewById(1L)).thenReturn(true);
        this.mockMvc.perform(
                get("/reviews/1/delete")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews?successfullyChanged"));
    }

    @Test
    @WithMockUser
    void getNewStatusReview_returnRedirect() throws Exception {
        when(reviewService.changeStatus(1L)).thenReturn(true);
        this.mockMvc.perform(
                get("/reviews/1/newStatus")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews?successfullyChanged"));
    }

}