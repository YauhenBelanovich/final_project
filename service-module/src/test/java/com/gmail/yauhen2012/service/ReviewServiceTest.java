package com.gmail.yauhen2012.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.yauhen2012.repository.ReviewRepository;
import com.gmail.yauhen2012.repository.model.Review;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.impl.ReviewServiceImpl;
import com.gmail.yauhen2012.service.model.AddReviewDTO;
import com.gmail.yauhen2012.service.model.ReviewDTO;
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
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    private ReviewService reviewService;

    private static final Long TEST_ID = 1L;

    @BeforeEach
    public void setup() {
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    public void addReview_verifyCallMethod() {
        AddReviewDTO addReviewDTO = new AddReviewDTO();
        addReviewDTO.setUserId(TEST_ID);
        addReviewDTO.setText("text");
        addReviewDTO.setStatus(true);

        reviewService.add(addReviewDTO);
        verify(reviewRepository, times(1)).add(any());
    }

    @Test
    public void changeStatus_verifyChanging() {
        Review review = setReview();
        Review reviewFromDb = setReview();
        when(reviewRepository.findById(TEST_ID)).thenReturn(reviewFromDb);
        reviewService.changeStatus(TEST_ID);
        verify(reviewRepository, times(1)).merge(reviewFromDb);

        Assertions.assertThat(review.getStatus()).isNotEqualTo(reviewFromDb.getStatus());
    }

    @Test
    public void deleteReview_verifyCallMethod() {
        Review review = setReview();
        when(reviewRepository.findById(TEST_ID)).thenReturn(review);
        reviewService.deleteReviewById(TEST_ID);
        verify(reviewRepository, times(1)).remove(any());
    }

    @Test
    public void findReviewByID_returnReview() {
        Review review = setReview();
        when(reviewRepository.findById(TEST_ID)).thenReturn(review);
        ReviewDTO reviewDTO = reviewService.findReviewById(TEST_ID);

        Assertions.assertThat(review.getText().equals(reviewDTO.getText()));
        verify(reviewRepository, times(1)).findById(anyLong());

        Assertions.assertThat(reviewDTO).isNotNull();

    }

    @Test
    public void findReviewsByPage_returnReviewList() {
        String page = "1";
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < PaginationConstant.ITEMS_BY_PAGE; i++) {
            Review review = setReview();
            reviews.add(review);
        }
        when(reviewRepository.getObjectsByPage(
                PaginationUtil.findStartPosition(Integer.parseInt(page)),
                PaginationConstant.ITEMS_BY_PAGE)).thenReturn(reviews);
        List<ReviewDTO> reviewDTOS = reviewService.getReviewByPage(page);

        Assertions.assertThat(reviews.size()).isEqualTo(reviewDTOS.size());
        verify(reviewRepository, times(1))
                .getObjectsByPage(
                        PaginationUtil.findStartPosition(Integer.parseInt(page)),
                        PaginationConstant.ITEMS_BY_PAGE);

        Assertions.assertThat(reviewDTOS).isNotEmpty();
    }

    private Review setReview() {
        Review review = new Review();
        review.setReviewId(TEST_ID);
        review.setUserId(TEST_ID);
        review.setStatus(true);
        review.setText("test");
        review.setCreatedBy(Date.from(Instant.now()));

        return review;
    }

}
