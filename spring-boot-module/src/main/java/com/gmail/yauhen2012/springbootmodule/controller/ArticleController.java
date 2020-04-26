package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.ArticleService;
import com.gmail.yauhen2012.service.CommentService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AddArticleDTO;
import com.gmail.yauhen2012.service.model.AppUser;
import com.gmail.yauhen2012.service.model.ArticleDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, UserService userService, CommentService commentService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
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

    @GetMapping("/{id}/delete")
    public String deleteArticleById(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        logger.debug("Get deleteArticleById method");
        return "redirect:/articles";
    }

    @GetMapping("/comment/{id}/delete")
    public String deleteCommentById(@PathVariable Long id) {
        Long articleId = commentService.deleteCommentById(id);
        logger.debug("Get deleteCommentById method");
        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/edit")
    public String articleEdit(@ModelAttribute(name = "article") ArticleDTO articleDTO, Model model) {
        articleService.update(articleDTO);
        logger.debug("Post addUser method");
        return "redirect:/articles/" + articleDTO.getArticleId();
    }

    @GetMapping("/new")
    public String addArticlePage(Model model) {
        model.addAttribute("article", new AddArticleDTO());
        logger.debug("Get addUserPage method");
        return "article_new";
    }

    @PostMapping("/new")
    public String addArticle(@Valid @ModelAttribute(name = "article") AddArticleDTO articleDTO, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("article", articleDTO);
            return "article_new";
        } else {

            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
                articleDTO.setUserId(getCurrentUserId());
                articleService.add(articleDTO);
                logger.debug("Post addArticle method");
                return "redirect:/articles";
            } else {
                return "redirect:/login?logout";
            }
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return userService.loadUserByEmail(user.getUsername()).getId();
    }

}
