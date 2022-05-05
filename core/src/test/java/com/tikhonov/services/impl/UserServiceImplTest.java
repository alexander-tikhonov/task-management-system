package com.tikhonov.services.impl;

import com.tikhonov.exceptions.UserServiceException;
import com.tikhonov.models.Authority;
import com.tikhonov.models.User;
import com.tikhonov.repositories.UserRepository;
import com.tikhonov.services.UserService;
import com.tikhonov.validators.FieldValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("Service for working with users")
@SpringBootTest
class UserServiceImplTest {

    private static final Long USER_ID = 5L;
    private static final String USER_EMAIL = "email@email.ru";
    private static final String USER_NAME = "Name";
    private static final String USER_PASSWORD = "secret";

    @Configuration
    @Import(UserServiceImpl.class)
    static class UserServiceImplTestConfig {}

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FieldValidator fieldValidator;

    private Set<Authority> authorities;

    @BeforeEach
    void setUp() {
        authorities = Set.of(new Authority("USER"));
    }

    @DisplayName(" should throw exception when user has incorrect data")
    @Test
    public void shouldThrowExceptionWhenUserHasIncorrectData() {
        var user = new User(USER_ID, null, USER_EMAIL, USER_PASSWORD, authorities);

        given(fieldValidator.validate(user)).willReturn(false);
        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(UserServiceException.class);
    }

    @DisplayName(" should correct save user")
    @Test
    public void shouldCorrectSaveUser() {
        var user = new User(null, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities);
        var expectedUser = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities);

        given(fieldValidator.validate(user)).willReturn(true);
        given(userRepository.save(user)).willReturn(expectedUser);

        assertThat(userService.create(user)).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @DisplayName(" should find existing user by id")
    @Test
    public void shouldFindExistingUserById() {
        var user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities);

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        assertThat(userService.findById(USER_ID)).get()
                .isEqualTo(user);
    }

    @DisplayName(" should find all users")
    @Test
    public void shouldFindAllUsers() {
        List<User> users = List.of(
                new User(5L, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities),
                new User(6L, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities),
                new User(7L, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities)
        );

        given(userRepository.findAll()).willReturn(users);
        assertThat(userService.findAll()).isNotNull();
    }

    @DisplayName(" should correct update user")
    @Test
    public void shouldCorrectUpdateUser() {
        var user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities);

        given(fieldValidator.validate(user)).willReturn(true);
        given(userRepository.save(user)).willReturn(user);

        assertThat(userService.update(user)).isNotNull();
    }

    @DisplayName(" should correct delete user by id")
    @Test
    public void shouldCorrectDeleteUserById() {
        assertThatNoException().isThrownBy(() -> userService.deleteById(USER_ID));
    }
}