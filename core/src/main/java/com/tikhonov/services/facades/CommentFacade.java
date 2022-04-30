package com.tikhonov.services.facades;

import com.tikhonov.models.dto.CommentRequestDto;
import com.tikhonov.models.dto.CommentResponseDto;

import java.util.List;

public interface CommentFacade {

    CommentResponseDto create(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> findAllByTaskId(Long id);

    void deleteById(Long id);
}
