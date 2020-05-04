package com.gmail.yauhen2012.repository.impl;

import java.util.List;

import javax.persistence.Query;

import com.gmail.yauhen2012.repository.ArticleRepository;
import com.gmail.yauhen2012.repository.model.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryImpl extends GenericDAOImpl<Long, Article> implements ArticleRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getArticleByPageSortedByDate(
            int startPosition,
            int itemsByPage
    ) {
        String query = "from " + entityClass.getName() + " a ORDER BY a.date DESC";
        Query q = entityManager.createQuery(query);
        q.setFirstResult(startPosition);
        q.setMaxResults(itemsByPage);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> findAllArticlesSortedByDate() {
        String query = "from " + entityClass.getName() + " a ORDER BY a.date DESC";
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }

}
