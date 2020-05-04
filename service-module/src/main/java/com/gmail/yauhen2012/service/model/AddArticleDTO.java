package com.gmail.yauhen2012.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AddArticleDTO {

    @NotEmpty(message = "is required")
    private String articleName;
    @NotEmpty(message = "is required")
    @Size(max = 1000, message = "Must be max 1000 characters long")
    private String text;
    private Long userId;

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        AddArticleDTO that = (AddArticleDTO) o;
        return Objects.equals(articleName, that.articleName) &&
                Objects.equals(text, that.text) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName, text, userId);
    }

}
