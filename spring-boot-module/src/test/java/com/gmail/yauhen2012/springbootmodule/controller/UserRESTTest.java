package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.springbootmodule.controller.config.TestAPIConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value = "/application-integration.properties")
@Import(TestAPIConfig.class)
@ActiveProfiles("test")
public class UserRESTTest {

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private UserService userService;

    @Test
    public void getUsers_return() throws Exception {
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .getForEntity("/api/users", String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @Test
    public void postUsers_return201() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String json = "{\n" +
                "    \"lastName\": \"userApi\",\n" +
                "    \"firstName\": \"firstUserApi\",\n" +
                "    \"patronymic\": \"patronymicUserApi\",\n" +
                "    \"role\": \"CUSTOMER_USER\",\n" +
                "    \"email\": \"yauhen2012@gmail.com\"\n" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/users", HttpMethod.POST, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    }

    @Test
    public void getUsers_return401() throws Exception {
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret123")
                .getForEntity("/api/users", String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
    }

}
