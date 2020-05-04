package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.ItemService;
import com.gmail.yauhen2012.service.model.ItemDTO;
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
public class ItemControllerSecurityITTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getItemsWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/items")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getItemsLikeCustomer_returnOk() throws Exception {
        mvc.perform(
                get("/items")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getItemsLikeSaleUser_returnOk() throws Exception {
        mvc.perform(
                get("/items")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getItemsLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/items")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }


    @Test
    public void getItemWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getItemLikeCustomer_returnOk() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(TEST_ID);
        itemDTO.setItemName("TestName");
        when(itemService.findById(TEST_ID)).thenReturn(itemDTO);
        mvc.perform(
                get("/items/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getItemLikeSaleUser_returnOk() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(TEST_ID);
        itemDTO.setItemName("TestName");
        when(itemService.findById(TEST_ID)).thenReturn(itemDTO);
        mvc.perform(
                get("/items/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getItemLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void deleteItemWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteItemLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteItemLikeSaleUser_returnOk() throws Exception {
        when(itemService.deleteItemById(TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/items/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/items?success"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteItemLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void copyItemWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/copy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void copyItemLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/copy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void copyItemLikeSaleUser_returnOk() throws Exception {
        when(itemService.copyItem(TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/items/1/copy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/items?success"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void copyItemLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/1/copy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void uploadItemsXMLWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/uploadFile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void uploadItemsXMLLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/uploadFile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void uploadItemsXMLLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/items/uploadFile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }
}
