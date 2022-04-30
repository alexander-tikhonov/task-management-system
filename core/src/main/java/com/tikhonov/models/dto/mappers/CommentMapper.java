package com.tikhonov.models.dto.mappers;


import com.tikhonov.models.Comment;
import com.tikhonov.models.dto.CommentResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDto toResponseDto(Comment comment);
}
