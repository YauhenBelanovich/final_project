package com.gmail.yauhen2012.repository.impl;

import com.gmail.yauhen2012.repository.CommentRepository;
import com.gmail.yauhen2012.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericDAOImpl<Long, Comment> implements CommentRepository {

}
