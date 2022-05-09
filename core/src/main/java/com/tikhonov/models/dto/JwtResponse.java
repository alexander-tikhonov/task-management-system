package com.tikhonov.models.dto;

import com.tikhonov.models.Authority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public class JwtResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8470959904473866838L;

    private String token;
    private UserResponseDto user;
    private Set<Authority> authorities;

    public JwtResponse() {}

    public JwtResponse(String token, UserResponseDto user, Set<Authority> authorities) {
        this.token = token;
        this.user = user;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }
}
