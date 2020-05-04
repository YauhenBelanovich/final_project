package com.gmail.yauhen2012.repository.impl;

import java.util.List;

import javax.persistence.Query;

import com.gmail.yauhen2012.repository.ReviewRepository;
import com.gmail.yauhen2012.repository.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends GenericDAOImpl<Long, Review> implements ReviewRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> getActiveReviewsSortedByDate() {
        String query = "from " + entityClass.getName() + " r WHERE r.status=true ORDER BY r.createdBy DESC";
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }

}
