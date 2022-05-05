package com.tikhonov.models.dto.mappers;

import com.tikhonov.models.User;
import com.tikhonov.models.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);
}
