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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemControllerITTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void getItems_returnAllItems() throws Exception {
        mvc.perform(
                get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void getNotExistItem_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/5")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "5")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/?error"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void getItem_returnOk() throws Exception {
        mvc.perform(
                get("/items/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isOk()).andExpect(view().name("item"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void deleteItem_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/items?success"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void copyItem_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/copy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/items?success"));
    }
}
