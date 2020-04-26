package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProfileControllerITTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    @Sql({"/data.sql"})
    public void updateUserInfo_returnRedirect() throws Exception {
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(1L);
        userInformationDTO.setAddress("testAddress");
        mvc.perform(
                post("/profile/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isFound());
    }

}
