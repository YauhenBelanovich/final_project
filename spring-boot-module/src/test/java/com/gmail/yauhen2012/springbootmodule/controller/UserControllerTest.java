package com.gmail.yauhen2012.springbootmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void getUsersPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/users")
        ).andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void getAddUsersPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/users/new")
        ).andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void addUser_returnRedirect() throws Exception {
        AddUserDTO user = new AddUserDTO();
        user.setPassword("test");
        user.setEmail("test@test.com");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPatronymic("testPatronymic");
        user.setRole(RoleEnum.CUSTOMER_USER);

        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(
                post("/users/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(content)
        ).andExpect(status().isOk());
    }

}