package com.gmail.yauhen2012.repository.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private Long articleId;
    @Column(name = "article_name")
    private String articleName;
    @Basic(optional = false)
    @Column(name = "date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column
    private String summary;

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private ArticleContent articleContent;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST, CascadeType.MERGE
            }, orphanRemoval = true)
    @JoinColumn(name = "article_id")
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ArticleContent getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(ArticleContent articleContent) {
        this.articleContent = articleContent;
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
        Article article = (Article) o;
        return Objects.equals(articleId, article.articleId) &&
                Objects.equals(articleName, article.articleName) &&
                Objects.equals(date, article.date) &&
                Objects.equals(summary, article.summary) &&
                Objects.equals(articleContent, article.articleContent) &&
                Objects.equals(userId, article.userId) &&
                Objects.equals(comments, article.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, articleName, date, summary, articleContent, userId, comments);
    }

}
