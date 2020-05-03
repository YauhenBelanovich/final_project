package com.gmail.yauhen2012.service;

import com.gmail.yauhen2012.service.model.AddCommentDTO;

public interface CommentService {

    void add(AddCommentDTO addCommentDTO);

    Long deleteCommentById(Long id);

}
