package com.gmail.yauhen2012.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.yauhen2012.repository.ArticleRepository;
import com.gmail.yauhen2012.repository.model.Article;
import com.gmail.yauhen2012.repository.model.ArticleContent;
import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.impl.ArticleServiceImpl;
import com.gmail.yauhen2012.service.model.AddArticleDTO;
import com.gmail.yauhen2012.service.model.ArticleDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    private ArticleService articleService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_TEXT = "Test test test";
    private static final String TEST_PAGE = "0";
    private static final int TEST_PAGE_INT = 0;


    @BeforeEach
    public void setup() {
        articleService = new ArticleServiceImpl(articleRepository);
    }

    @Test
    public void saveArticle_returnCallMethod() {
        AddArticleDTO addArticleDTO = setAddArticleDTO();
        articleService.add(addArticleDTO);
        verify(articleRepository, times(1)).add(any());
    }

        @Test
        public void findArticleByID_returnArticle() {
            Article article = setArticle();
            when(articleRepository.findById(TEST_ID)).thenReturn(article);
            ArticleDTO articleDTO = articleService.findById(TEST_ID);

            Assertions.assertThat(articleDTO.getArticleName().equals(article.getArticleName()));
            verify(articleRepository, times(1)).findById(anyLong());

            Assertions.assertThat(articleDTO).isNotNull();

        }

    @Test
    public void deleteArticle_verifyCallMethod() {

        Article article = setArticle();
        when(articleRepository.findById(TEST_ID)).thenReturn(article);
        articleService.deleteArticleById(TEST_ID);
        verify(articleRepository, times(1)).remove(any());
    }

    @Test
    public void findArticlesByPage_returnArticlesList() {

        List<Article> articles = new ArrayList<>();
        for (long i = 0; i < PaginationConstant.ITEMS_BY_PAGE; i++) {
            Article article = setArticle();
            article.setArticleId(i);
            articles.add(article);
        }
        when(articleRepository.getArticleByPageSortedByDate(PaginationUtil.findStartPosition(TEST_PAGE_INT), PaginationConstant.ITEMS_BY_PAGE)).thenReturn(articles);
        List<ArticleDTO> articleDTOS = articleService.getArticlesWithoutContentByPage(TEST_PAGE);

        Assertions.assertThat(articles.size()).isEqualTo(articleDTOS.size());
        verify(articleRepository, times(1))
                .getArticleByPageSortedByDate(PaginationUtil.findStartPosition(TEST_PAGE_INT), PaginationConstant.ITEMS_BY_PAGE);
        Assertions.assertThat(articleDTOS).isNotEmpty();
    }

    @Test
    public void findArticles_returnArticlesList() {

        List<Article> articles = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            Article article = setArticle();
            article.setArticleId(i);
            articles.add(article);
        }
        when(articleRepository.findAllArticlesSortedByDate()).thenReturn(articles);
        List<ArticleDTO> articleDTOS = articleService.findAllSorted();

        verify(articleRepository, times(1))
                .findAllArticlesSortedByDate();
        Assertions.assertThat(articleDTOS).isNotEmpty();
    }

    private Article setArticle() {
        Article article = new Article();
        article.setArticleName(TEST_TEXT);
        article.setUserId(TEST_ID);
        article.setSummary(TEST_TEXT);
        article.setDate(new Date());
        ArticleContent articleContent = new ArticleContent();
        articleContent.setText(TEST_TEXT);
        article.setArticleContent(articleContent);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comments.add(comment);
        article.setComments(comments);
        return article;
    }

    private AddArticleDTO setAddArticleDTO() {
        AddArticleDTO article = new AddArticleDTO();
        article.setArticleName(TEST_TEXT);
        article.setUserId(TEST_ID);
        article.setText(TEST_TEXT);
        return article;
    }

}
