package com.gmail.yauhen2012.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddArticleDTO {

    @NotEmpty(message = "is required")
    private String articleName;
    @Size(max = 1000, message = "Must be max 1000 characters long")
    private String text;
    @NotNull(message = "is required")
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

}
