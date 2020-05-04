package com.gmail.yauhen2012.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.ReviewRepository;
import com.gmail.yauhen2012.repository.model.Review;
import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.constant.ReviewConstant;
import com.gmail.yauhen2012.service.model.AddReviewDTO;
import com.gmail.yauhen2012.service.model.ReviewDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void add(AddReviewDTO addReviewDTO) {
        Review review = convertAddReviewDTOToDatabaseReview(addReviewDTO);
        reviewRepository.add(review);
    }

    @Override
    @Transactional
    public List<ReviewDTO> getReviewByPage(String page) {
        int pageInt = Integer.parseInt(page);
        List<Review> reviewList = reviewRepository.getObjectsByPage(
                PaginationUtil.findStartPosition(pageInt),
                PaginationConstant.ITEMS_BY_PAGE
        );
        return reviewList.stream()
                .map(this::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDTO findReviewById(Long id) {
        Review review = reviewRepository.findById(id);
        return convertDatabaseObjectToDTO(review);
    }

    @Override
    @Transactional
    public Boolean deleteReviewById(Long id) {
        Review review = reviewRepository.findById(id);
        if (review != null) {
            reviewRepository.remove(review);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean changeStatus(Long id) {
        Review review = reviewRepository.findById(id);
        if (review != null) {
            if (review.getStatus()) {
                review.setStatus(false);
            } else {
                review.setStatus(true);
            }
            reviewRepository.merge(review);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public List<ReviewDTO> getReviewSortedByDate() {
        List<Review> reviews = reviewRepository.getActiveReviewsSortedByDate();
        return reviews.stream()
                .map(this::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO convertDatabaseObjectToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setText(review.getText());
        reviewDTO.setStatus(review.getStatus());
        reviewDTO.setCreatedBy(review.getCreatedBy().toString());
        reviewDTO.setUserId(review.getUserId());
        return reviewDTO;
    }

    private Review convertAddReviewDTOToDatabaseReview(AddReviewDTO addReviewDTO) {
        Review review = new Review();
        review.setText(addReviewDTO.getText());
        review.setStatus(ReviewConstant.DEFAULT_REVIEW_STATUS);
        review.setUserId(addReviewDTO.getUserId());
        return review;
    }

}
