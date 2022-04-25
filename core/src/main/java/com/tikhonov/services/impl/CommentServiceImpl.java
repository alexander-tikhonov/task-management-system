package com.tikhonov.services.impl;

import com.tikhonov.exceptions.CommentServiceException;
import com.tikhonov.models.Comment;
import com.tikhonov.repositories.CommentRepository;
import com.tikhonov.services.CommentService;
import com.tikhonov.validators.FieldValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FieldValidator fieldValidator;

    public CommentServiceImpl(CommentRepository commentRepository, FieldValidator fieldValidator) {
        this.commentRepository = commentRepository;
        this.fieldValidator = fieldValidator;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Comment create(Comment comment) {
        checkComment(comment);
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllByTaskId(Long id) {
        return commentRepository.findAllByTask_Id(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    private void checkComment(Comment comment) {
        if (!fieldValidator.validate(comment)) {
            throw new CommentServiceException("Incorrect comment format!");
        }
    }
}
