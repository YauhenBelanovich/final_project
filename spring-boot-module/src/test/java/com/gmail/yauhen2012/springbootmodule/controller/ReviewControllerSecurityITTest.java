package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
public class ReviewControllerSecurityITTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ReviewService reviewService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getReviewsWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getReviews_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getReviews_returnOk() throws Exception {
        mvc.perform(
                get("/reviews")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getReviewsLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    public void deleteReviewWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteReviewLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteReviewLikeAdmin_returnOk() throws Exception {
        when(reviewService.deleteReviewById(TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/reviews/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews?successfullyChanged"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteReviewLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    public void newStatusReviewWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void newStatusReviewLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void newStatusReviewLikeAdmin_returnOk() throws Exception {
        when(reviewService.changeStatus(TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/reviews/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews?successfullyChanged"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void newStatusReviewLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }
}
