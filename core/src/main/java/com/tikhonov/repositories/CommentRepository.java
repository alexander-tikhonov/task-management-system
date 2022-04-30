package com.tikhonov.repositories;

import com.tikhonov.models.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(value = "comments-eg")
    List<Comment> findAllByTask_Id(Long id);
}
