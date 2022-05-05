package com.tikhonov.models.dto.mappers;

import com.tikhonov.models.Task;
import com.tikhonov.models.dto.TaskResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TaskMapper {

    TaskResponseDto toResponseDto(Task task);
}
