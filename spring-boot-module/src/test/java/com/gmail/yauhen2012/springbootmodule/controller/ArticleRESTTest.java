package com.gmail.yauhen2012.springbootmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhen2012.service.model.AddArticleDTO;
import com.gmail.yauhen2012.springbootmodule.controller.config.TestAPIConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ArticleRESTTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getArticles_return403() throws Exception {
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .getForEntity("/api/articles", String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @Test
    public void getArticles_return200() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/articles", HttpMethod.GET, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void getArticles_return401() throws Exception {
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret123")
                .getForEntity("/api/articles", String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
    }

    @Test
    public void postArticles_return201() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        AddArticleDTO addArticleDTO = new AddArticleDTO();
        addArticleDTO.setText("test");
        addArticleDTO.setArticleName("test");
        addArticleDTO.setUserId(1L);
        String content = objectMapper.writeValueAsString(addArticleDTO);

        HttpEntity<String> httpEntity = new HttpEntity<>(content, httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/articles", HttpMethod.POST, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    }

    @Test
    public void getArticle_return403() throws Exception {
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .getForEntity("/api/articles/1", String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @Test
    public void deleteArticle_return200() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/articles/1", HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void deleteArticle_return401() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .exchange("/api/articles/1", HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

}
