package com.gmail.yauhen2012.service.impl;

import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.CommentRepository;
import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.CommentService;
import com.gmail.yauhen2012.service.model.AddCommentDTO;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {this.commentRepository = commentRepository;}

    @Override
    @Transactional
    public void add(AddCommentDTO addCommentDTO) {
        Comment comment = convertAddCommentDTOToDatabaseComment(addCommentDTO);
        commentRepository.add(comment);
    }

    @Override
    @Transactional
    public Long deleteCommentById(Long id) {
        Comment comment = commentRepository.findById(id);
        commentRepository.remove(comment);
        return comment.getArticleId();
    }

    private static Comment convertAddCommentDTOToDatabaseComment(AddCommentDTO addCommentDTO) {
        Comment comment = new Comment();
        comment.setArticleId(addCommentDTO.getArticleId());
        comment.setUserId(addCommentDTO.getUserId());
        comment.setText(addCommentDTO.getText());
        return comment;
    }

}
