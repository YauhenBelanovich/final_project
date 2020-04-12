package com.gmail.yauhen2012.service.model;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddReviewDTO {

    @Size(min = 3, max = 40,
            message = "Must be between 3 and 40 characters long")
    private String text;
    @NotNull(message = "is required")
    private Boolean status;
    @NotNull(message = "is required")
    private Long userId;

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
        AddReviewDTO that = (AddReviewDTO) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(status, that.status) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, status, userId);
    }

}
