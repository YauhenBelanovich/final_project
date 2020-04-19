package com.gmail.yauhen2012.repository;

import java.util.List;

import com.gmail.yauhen2012.repository.model.Article;

public interface ArticleRepository extends GenericDAO<Long, Article> {

    List<Article> getArticleByPageSortedByDate(int startPosition, int itemsByPage);

    List<Article> findAllArticlesSortedByDate();
}
