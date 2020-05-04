package com.gmail.yauhen2012.service;

import java.util.List;

import com.gmail.yauhen2012.service.model.AddReviewDTO;
import com.gmail.yauhen2012.service.model.ReviewDTO;

public interface ReviewService {

    void add(AddReviewDTO addReviewDTO);

    List<ReviewDTO> getReviewByPage(String page);

    ReviewDTO findReviewById(Long id);

    Boolean deleteReviewById(Long id);

    Boolean changeStatus(Long id);

    List<ReviewDTO> getReviewSortedByDate();

}
