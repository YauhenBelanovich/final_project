package com.gmail.yauhen2012.springbootmodule.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhen2012.service.ArticleService;
import com.gmail.yauhen2012.service.model.AddArticleDTO;
import com.gmail.yauhen2012.service.model.ArticleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleAPIController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleService articleService;

    public ArticleAPIController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object saveArticle(@RequestBody @Valid AddArticleDTO addArticleDTO, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return getValidationMessage(bindingResult);
        }
        logger.debug("POST API save Article method");
        articleService.add(addArticleDTO);
        return "Added Successfully";
    }

    @GetMapping
    public List<ArticleDTO> getArticles() {
        logger.debug("Get API getArticles method");
        return articleService.findAllSorted();
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticle(@PathVariable Long id) {
        logger.debug("Get API getArticle method");
        return articleService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        logger.debug("DELETE API deleteArticle method");
        return "Deleted Successfully";
    }

    private String getValidationMessage(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        List<String> message = new ArrayList<>();
        for (FieldError e : errors) {
            message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
        }
        return message.toString();
    }

}
