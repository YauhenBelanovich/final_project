package com.gmail.yauhen2012.repository;

import java.util.List;

import com.gmail.yauhen2012.repository.model.Comment;

public interface CommentRepository extends GenericDAO<Long, Comment> {

    List<Comment> getCommentByPageSortedByDate(int startPosition, int itemsByPage);
}
