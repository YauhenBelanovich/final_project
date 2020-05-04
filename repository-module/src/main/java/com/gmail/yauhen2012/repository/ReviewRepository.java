package com.gmail.yauhen2012.repository;

import java.util.List;

import com.gmail.yauhen2012.repository.model.Review;

public interface ReviewRepository extends GenericDAO<Long, Review> {

    List<Review> getActiveReviewsSortedByDate();

}
