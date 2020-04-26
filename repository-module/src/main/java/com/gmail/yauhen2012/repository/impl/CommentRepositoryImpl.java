package com.gmail.yauhen2012.repository.impl;

import java.util.List;
import javax.persistence.Query;

import com.gmail.yauhen2012.repository.CommentRepository;
import com.gmail.yauhen2012.repository.model.Article;
import com.gmail.yauhen2012.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericDAOImpl<Long, Comment> implements CommentRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Comment> getCommentByPageSortedByDate(
            int startPosition,
            int itemsByPage
    ) {
        String query = "from " + entityClass.getName() + " a ORDER BY a.date DESC";
        Query q = entityManager.createQuery(query);
        q.setFirstResult(startPosition);
        q.setMaxResults(itemsByPage);

        return q.getResultList();
    }
}
