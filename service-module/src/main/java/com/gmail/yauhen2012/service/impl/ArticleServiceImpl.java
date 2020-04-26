package com.gmail.yauhen2012.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.ArticleRepository;
import com.gmail.yauhen2012.repository.model.Article;
import com.gmail.yauhen2012.repository.model.ArticleContent;
import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.ArticleService;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.model.AddArticleDTO;
import com.gmail.yauhen2012.service.model.ArticleDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private static final int ARTICLE_SUMMARY_LIMIT = 200;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticlesWithoutContentByPage(String page) {
        int pageInt = Integer.parseInt(page);
        List<Article> articleList = articleRepository.getArticleByPageSortedByDate(
                PaginationUtil.findStartPosition(pageInt),
                PaginationConstant.ITEMS_BY_PAGE
        );
        return articleList.stream()
                .map(this::convertDatabaseArticleWithoutContentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void add(AddArticleDTO addArticleDTO) {

        Article article = convertAddArticleDTOToDatabaseArticle(addArticleDTO);
        articleRepository.add(article);

        ArticleContent articleContent = new ArticleContent();
        articleContent.setText(addArticleDTO.getText());

        articleContent.setArticle(article);
        article.setArticleContent(articleContent);
    }

    @Override
    @Transactional
    public ArticleDTO findById(Long id) {
        Article article = articleRepository.findById(id);
        return convertDatabaseArticleWithContentToDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> findAllSorted() {
        List<Article> articles = articleRepository.findAllArticlesSortedByDate();
        return articles.stream()
                .map(this::convertDatabaseArticleWithoutContentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteArticleById(Long id) {
        Article article = articleRepository.findById(id);
        articleRepository.remove(article);
    }

    @Override
    @Transactional
    public void update(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getArticleId());

        article.setArticleName(articleDTO.getArticleName());
        ArticleContent articleContent = article.getArticleContent();
        articleContent.setText(articleDTO.getText());
        article.setArticleContent(articleContent);
        article.setSummary(getSummary(articleDTO.getText()));

        articleRepository.merge(article);
    }

    private ArticleDTO convertDatabaseArticleWithoutContentToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleId(article.getArticleId());
        articleDTO.setArticleName(article.getArticleName());
        articleDTO.setDate(article.getDate().toString());
        articleDTO.setUserId(article.getUserId());
        articleDTO.setSummary(article.getSummary());
        return articleDTO;
    }

    private ArticleDTO convertDatabaseArticleWithContentToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleId(article.getArticleId());
        articleDTO.setArticleName(article.getArticleName());
        articleDTO.setDate(article.getDate().toString());
        articleDTO.setUserId(article.getUserId());
        articleDTO.setText(article.getArticleContent().getText());
        List<Comment> comments = article.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getDate).reversed())
                .collect(Collectors.toList());
        articleDTO.setComments(comments);
        return articleDTO;
    }

    private Article convertAddArticleDTOToDatabaseArticle(AddArticleDTO addArticleDTO) {
        Article article = new Article();
        article.setArticleName(addArticleDTO.getArticleName());
        article.setUserId(addArticleDTO.getUserId());
        article.setSummary(getSummary(addArticleDTO.getText()));
        return article;
    }

    private String getSummary(String text) {
        if (text.codePointCount(0, text.length()) > ARTICLE_SUMMARY_LIMIT) {
            return text.substring(0, text.offsetByCodePoints(0, ARTICLE_SUMMARY_LIMIT - 3)) + "...";
        } else {
            return text;
        }
    }

}
