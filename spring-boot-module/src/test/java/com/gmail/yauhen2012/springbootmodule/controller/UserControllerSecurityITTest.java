package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
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
public class UserControllerSecurityITTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getUsersWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getUsersLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getUsersLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getUsers_returnOk() throws Exception {
        mvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getUserLikeAdmin_returnOk() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        when(userService.findUserById(1L)).thenReturn(userDTO);
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(1L);
        when(userService.findUserInformationById(1L)).thenReturn(userInformationDTO);
        mvc.perform(
                get("/users/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getUserLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getUserLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void deleteUserWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteUserLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteUserLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void newPasswordWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void newPasswordLikeAdmin_returnRedirect() throws Exception {

        when(userService.changePassword(1L)).thenReturn(true);
        mvc.perform(
                get("/users/1/newPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users?successfullyChanged"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void newPasswordLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void newPasswordLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void newRoleWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newRole")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void newRoleLikeAdmin_returnRedirect() throws Exception {

        when(userService.changeRole(1L, RoleEnum.CUSTOMER_USER)).thenReturn(true);
        mvc.perform(
                get("/users/1/newRole")
                        .param("id", "1")
                        .param("role", "CUSTOMER_USER")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users?successfullyChanged"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void newRoleLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newRole")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void newRoleLikeSaleUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/users/1/newRole")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

}
