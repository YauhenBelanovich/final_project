package com.gmail.yauhen2012.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AddReviewDTO {

    @NotEmpty
    @Size(min = 3, max = 300,
            message = "Must be between 3 and 300 characters long")
    private String text;
    private Boolean status;
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
