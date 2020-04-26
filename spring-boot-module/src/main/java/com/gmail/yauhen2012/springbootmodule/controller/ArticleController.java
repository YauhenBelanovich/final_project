package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.ArticleService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.ArticleDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping
    public String getArticleList(@RequestParam(value = "page", defaultValue = "1") String page, Model model) {

        List<ArticleDTO> articleDTOList = articleService.getArticlesWithoutContentByPage(page);
        List<UserDTO> userDTOList = articleDTOList.stream()
                .map(ArticleDTO::getUserId)
                .collect(Collectors.toSet())
                .stream()
                .map(userService::findUserById)
                .collect(Collectors.toList());
        model.addAttribute("articles", articleDTOList);
        model.addAttribute("users", userDTOList);
        logger.debug("Get ArticlesList method");
        return "articles";
    }

    @GetMapping("/{id}")
    public String getArticleById(@PathVariable Long id, Model model) {
        ArticleDTO articleDTO = articleService.findById(id);
        UserDTO userDTO = userService.findUserById(articleDTO.getUserId());
        List<UserDTO> userDTOS = articleDTO.getComments()
                .stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet())
                .stream()
                .map(userService::findUserById)
                .collect(Collectors.toList());
        model.addAttribute("article", articleDTO);
        model.addAttribute("user", userDTO);
        model.addAttribute("users", userDTOS);
        logger.debug("Get articleById method");
        return "article";
    }
}
