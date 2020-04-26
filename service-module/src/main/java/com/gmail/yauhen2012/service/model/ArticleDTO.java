package com.gmail.yauhen2012.service.model;

import java.util.List;
import java.util.Objects;

import com.gmail.yauhen2012.repository.model.Comment;

public class ArticleDTO {

    private Long articleId;
    private String articleName;
    private String date;
    private String summary;
    private String text;
    private Long userId;
    private List<Comment> comments;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArticleDTO that = (ArticleDTO) o;
        return Objects.equals(articleId, that.articleId) &&
                Objects.equals(articleName, that.articleName) &&
                Objects.equals(date, that.date) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(text, that.text) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, articleName, date, summary, text, userId, comments);
    }

}
