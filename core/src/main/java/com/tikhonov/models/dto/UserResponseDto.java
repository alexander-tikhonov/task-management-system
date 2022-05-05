package com.tikhonov.models.dto;

import java.io.Serial;
import java.io.Serializable;

public class UserResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1323790912450626268L;

    private Long id;
    private String name;
    private String email;

    public UserResponseDto() {}

    public UserResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
