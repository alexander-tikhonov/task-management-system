package com.tikhonov.controllers.api;

import com.tikhonov.exceptions.NotFoundException;
import com.tikhonov.models.Authority;
import com.tikhonov.models.dto.JwtResponse;
import com.tikhonov.models.dto.LoginUser;
import com.tikhonov.models.dto.mappers.UserMapper;
import com.tikhonov.security.jwt.JwtProvider;
import com.tikhonov.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider,
                          UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userMapper = userMapper;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginUser loginUser) {
        var user = userService.findByEmail(loginUser.getUsername()).orElseThrow(
                () -> new NotFoundException("User not found!")
        );

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtProvider.generateToken(loginUser.getUsername());
        Set<Authority> authorities = user.getAuthorities();
        var response = new JwtResponse(jwt, userMapper.toResponseDto(user), authorities);
        return ResponseEntity.ok(response);
    }
}
