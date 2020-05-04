package com.gmail.yauhen2012.service;

import com.gmail.yauhen2012.repository.CommentRepository;
import com.gmail.yauhen2012.repository.model.Comment;
import com.gmail.yauhen2012.service.impl.CommentServiceImpl;
import com.gmail.yauhen2012.service.model.AddCommentDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    private CommentService commentService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_TEXT = "Test test test";

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

    @Test
    public void deleteArticle_verifyCallMethod() {
        Comment comment = setComment();
        when(commentRepository.findById(TEST_ID)).thenReturn(comment);
        Long l = commentService.deleteCommentById(TEST_ID);
        verify(commentRepository, times(1)).remove(any());
        Assertions.assertThat(l).isEqualTo(TEST_ID);
    }

    private Comment setComment() {
        Comment comment = new Comment();
        comment.setCommentId(TEST_ID);
        comment.setUserId(TEST_ID);
        comment.setText(TEST_TEXT);
        comment.setArticleId(TEST_ID);
        return comment;
    }
}
