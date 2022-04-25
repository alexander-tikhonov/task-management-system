package com.tikhonov.services.impl;

import com.tikhonov.exceptions.UserServiceException;
import com.tikhonov.models.User;
import com.tikhonov.repositories.UserRepository;
import com.tikhonov.services.UserService;
import com.tikhonov.validators.FieldValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FieldValidator fieldValidator;

    public UserServiceImpl(UserRepository userRepository, FieldValidator fieldValidator) {
        this.userRepository = userRepository;
        this.fieldValidator = fieldValidator;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User create(User user) {
        checkUser(user);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User update(User user) {
        checkUser(user);
        return userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private void checkUser(User user) {
        if (!fieldValidator.validate(user)) {
            throw new UserServiceException("Incorrect user data!");
        }
    }
}
