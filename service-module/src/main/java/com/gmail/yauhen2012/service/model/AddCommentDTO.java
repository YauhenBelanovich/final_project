package com.gmail.yauhen2012.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AddCommentDTO {

    @NotEmpty(message = "is required")
    private Long articleId;
    @NotEmpty(message = "is required")
    private Long userId;
    @Size(max = 200, message = "Must be max 200 characters long")
    private String text;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
