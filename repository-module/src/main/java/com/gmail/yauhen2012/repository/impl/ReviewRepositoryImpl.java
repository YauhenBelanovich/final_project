package com.gmail.yauhen2012.repository.impl;

import com.gmail.yauhen2012.repository.ReviewRepository;
import com.gmail.yauhen2012.repository.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends GenericDAOImpl<Long, Review> implements ReviewRepository {
}
