package com.tikhonov.models.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

public class LoginUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 3566157869710348348L;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginUser() {
    }

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
