package com.tikhonov.controllers.api;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.User;
import com.tikhonov.models.dto.UserResponseDto;
import com.tikhonov.models.dto.mappers.UserMapper;
import com.tikhonov.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUserByNameStartsWith(
            @RequestParam(value = "name") String name) {

        var userExample = new User();
        userExample.setName(name);

        return ResponseEntity.ok(userService.findAll(userExample).stream()
                .map(userMapper::toResponseDto).toList());
    }
}
