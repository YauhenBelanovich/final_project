package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserInfoController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class UserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getUserInfoPage_returnOk() throws Exception {
        when(userService.findUserById(1L)).thenReturn(new UserDTO());
        when(userService.findUserInformationById(1L)).thenReturn(new UserInformationDTO());
        this.mockMvc.perform(
                get("/info/user/1")
        ).andExpect(status().isOk());
    }

}