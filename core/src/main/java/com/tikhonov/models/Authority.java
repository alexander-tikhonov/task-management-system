package com.tikhonov.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "role", nullable = false)
    private String role;

    public Authority() {
    }

    public Authority(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
