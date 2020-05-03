package com.gmail.yauhen2012.service.model;

import java.util.Objects;

public class CommentDTO {

    private Long commentId;
    private Long articleId;
    private Long userId;
    private String text;
    private String date;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentDTO that = (CommentDTO) o;
        return Objects.equals(commentId, that.commentId) &&
                Objects.equals(articleId, that.articleId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(text, that.text) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, articleId, userId, text, date);
    }

}
