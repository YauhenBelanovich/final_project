package com.gmail.yauhen2012.service.model;

import java.util.Objects;

public class ReviewDTO {

    private Long reviewId;
    private String text;
    private Boolean status;
    private String createdBy;
    private Long userId;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewDTO reviewDTO = (ReviewDTO) o;
        return Objects.equals(reviewId, reviewDTO.reviewId) &&
                Objects.equals(text, reviewDTO.text) &&
                Objects.equals(status, reviewDTO.status) &&
                Objects.equals(createdBy, reviewDTO.createdBy) &&
                Objects.equals(userId, reviewDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, text, status, createdBy, userId);
    }

}
