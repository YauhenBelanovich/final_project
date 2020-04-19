package com.gmail.yauhen2012.service;

import com.gmail.yauhen2012.repository.CommentRepository;
import com.gmail.yauhen2012.service.impl.CommentServiceImpl;
import com.gmail.yauhen2012.service.model.AddCommentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    private CommentService commentService;

    private static final Long TEST_ID = 1L;

    @BeforeEach
    public void setup() {
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    public void saveComment_returnCallMethod() {

        AddCommentDTO addCommentDTO = new AddCommentDTO();
        addCommentDTO.setUserId(TEST_ID);
        addCommentDTO.setText("testText");
        commentService.add(addCommentDTO);
        verify(commentRepository, times(1)).add(any());
    }
}
