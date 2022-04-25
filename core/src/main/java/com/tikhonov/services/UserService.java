package com.tikhonov.services;

import com.tikhonov.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User create(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    User update(User user);

    void deleteById(Long id);
}
