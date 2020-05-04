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
public class ReviewControllerITTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void deleteReview_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews?successfullyChanged"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void changeStatus_returnRedirect() throws Exception {
        mvc.perform(
                get("/reviews/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("review_id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews?successfullyChanged"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void getReviews_returnAllReviews() throws Exception {
        mvc.perform(
                get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void getReviewsNewPage_returnAllReviews() throws Exception {
        mvc.perform(
                get("/reviews/new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
