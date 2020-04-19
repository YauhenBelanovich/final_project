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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerITTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void deleteUser_returnRedirect() throws Exception {
        mvc.perform(
                post("/users/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void generatePassword_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void createRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newRole")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
                        .param("role", "CUSTOMER_USER")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }

        @Test
        @WithMockUser(roles = "ADMINISTRATOR")
        @Sql({"/data.sql"})
        public void createUser_returnRedirect() throws Exception {
            mvc.perform(
                    post("/users/new")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                            .param("role", "CUSTOMER_USER")
                    .param("email", "test@test.test")
            ).andExpect(status().isOk());
        }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void getDocuments_returnAllDocuments() throws Exception {
        mvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
