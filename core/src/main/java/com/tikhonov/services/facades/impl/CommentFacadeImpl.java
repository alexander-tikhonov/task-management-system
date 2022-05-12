package com.tikhonov.services.facades.impl;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.Comment;
import com.tikhonov.models.Task;
import com.tikhonov.models.User;
import com.tikhonov.models.dto.CommentRequestDto;
import com.tikhonov.models.dto.CommentResponseDto;
import com.tikhonov.models.dto.mappers.CommentMapper;
import com.tikhonov.security.user.CustomUserDetails;
import com.tikhonov.services.CommentService;
import com.tikhonov.services.TaskService;
import com.tikhonov.services.UserService;
import com.tikhonov.services.facades.CommentFacade;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentFacadeImpl implements CommentFacade {

    private final CommentService commentService;
    private final UserService userService;
    private final TaskService taskService;
    private final CommentMapper commentMapper;

    public CommentFacadeImpl(CommentService commentService,
                             UserService userService,
                             TaskService taskService,
                             CommentMapper commentMapper) {
        this.commentService = commentService;
        this.userService = userService;
        this.taskService = taskService;
        this.commentMapper = commentMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public CommentResponseDto create(CommentRequestDto commentRequestDto, Authentication authentication) {
        final Long taskId = commentRequestDto.getTaskId();

        var user = getAuthUser(authentication);
        var task = getTaskById(taskId);

        var commentForSave = new Comment();
        commentForSave.setContent(commentRequestDto.getContent());
        commentForSave.setUser(user);
        commentForSave.setTask(task);

        return commentMapper.toResponseDto(commentService.create(commentForSave));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentResponseDto> findAllByTaskId(Long id) {
        return commentService.findAllByTaskId(id).stream()
                .map(commentMapper::toResponseDto)
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        commentService.deleteById(id);
    }

    private User getUserById(Long id) {
        return userService.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id: %d not found!", id))
        );
    }

    private Task getTaskById(Long id) {
        return taskService.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Task with id: %d not found!", id))
        );
    }

    private User getAuthUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
