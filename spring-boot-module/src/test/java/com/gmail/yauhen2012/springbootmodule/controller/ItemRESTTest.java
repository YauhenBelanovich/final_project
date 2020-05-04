package com.gmail.yauhen2012.springbootmodule.controller;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauhen2012.service.model.AddItemDTO;
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
public class ItemRESTTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getItems_return403() {
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .getForEntity("/api/items", String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @Test
    public void getItems_return200() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/items", HttpMethod.GET, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void getItems_return401() {
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret123")
                .getForEntity("/api/items", String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
    }

    @Test
    public void postItem_return201() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setItemName("testName");
        addItemDTO.setUniqueNumber("hfdjhfjd");
        addItemDTO.setDescription("hfdjhfjd");
        addItemDTO.setPrice(BigDecimal.TEN);
        String content = objectMapper.writeValueAsString(addItemDTO);

        HttpEntity<String> httpEntity = new HttpEntity<>(content, httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/items", HttpMethod.POST, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    }

    @Test
    public void getItem_return403() {
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .getForEntity("/api/items/1", String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

    @Test
    public void getItem_return200() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/items/1", HttpMethod.GET, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void getItem_return401() {
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret123")
                .getForEntity("/api/items/1", String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
    }

    @Test
    public void deleteItem_return200() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testAdmin", "secret")
                .exchange("/api/items/1", HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void deleteItem_return403() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> entity = template.withBasicAuth("testUser", "secret")
                .exchange("/api/items/1", HttpMethod.DELETE, httpEntity, String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

}
