package com.gmail.yauhen2012.springbootmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class UserControllerTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getUsersPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/users")
        ).andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    @WithMockUser
    void getAddUsersPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/users/new")
        ).andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    @WithMockUser
    void addUser_returnOk() throws Exception {
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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getUserPage_returnOk() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TEST_ID);
        when(userService.findUserById(TEST_ID)).thenReturn(userDTO);
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(TEST_ID);
        when(userService.findUserInformationById(TEST_ID)).thenReturn(userInformationDTO);
        this.mockMvc.perform(
                get("/users/1")
        ).andExpect(status().isOk())
                .andExpect(view().name("user_info"));
    }

    @Test
    @WithMockUser
    void newPassword_returnRedirect() throws Exception {
        when(userService.changePassword(TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/users/1/newPassword")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/users?successfullyChanged"));
    }

    @Test
    @WithMockUser
    void newRole_returnRedirect() throws Exception {
        when(userService.changeRole(TEST_ID, RoleEnum.CUSTOMER_USER)).thenReturn(true);
        this.mockMvc.perform(
                get("/users/1/newRole")
                .param("role", "CUSTOMER_USER")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/users?successfullyChanged"));
    }
}