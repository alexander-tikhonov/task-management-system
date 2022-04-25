package com.tikhonov.services;

import com.tikhonov.models.Comment;

import java.util.List;

public interface CommentService {

    Comment create(Comment comment);

    List<Comment> findAllByTaskId(Long id);

    void deleteById(Long id);
}
