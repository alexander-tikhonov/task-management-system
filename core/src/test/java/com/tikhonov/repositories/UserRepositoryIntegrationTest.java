package com.tikhonov.repositories;

import com.tikhonov.models.Authority;
import com.tikhonov.models.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository for working with users ")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {

    private static final String POSTGRESQL_CONTAINER_VERSION = "postgres:14.2";
    private static final String DB_NAME = "task_management_system";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "secret";

    private static final Long USER_ID = 5L;
    private static final String USER_NAME = "Ivan";
    private static final String USER_EMAIL = "ivan@mail.ru";
    private static final String USER_PASSWORD = "secret";
    public static final long AUTHORITY_ID = 2L;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(POSTGRESQL_CONTAINER_VERSION)
                    .withDatabaseName(DB_NAME)
                    .withUsername(DB_USER)
                    .withPassword(DB_PASSWORD);

    @TestConfiguration
    static class UserRepositoryIntegrationTestConfig {

        @Bean
        public DataSource dataSource() {
            var hikariConfig = new HikariConfig();

            hikariConfig.setUsername(postgreSQLContainer.getUsername());
            hikariConfig.setPassword(postgreSQLContainer.getPassword());
            hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());

            return new HikariDataSource(hikariConfig);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager em;

    private Set<Authority> authorities;

    @BeforeEach
    void setUp() {
        authorities = Set.of(em.find(Authority.class, AUTHORITY_ID));
    }

    @DisplayName(" should correct save user")
    @Transactional
    @Test
    public void shouldCorrectSaveUser() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();

        var user = new User(null, USER_NAME, USER_EMAIL, USER_PASSWORD, authorities);
        userRepository.save(user);

        em.flush();
        em.detach(user);

        var expectedUser = em.find(User.class, USER_ID);
        assertThat(expectedUser).isEqualTo(user);
    }

    @DisplayName(" should find user by name is like")
    @Transactional
    @Test
    public void shouldFindUserByNameIsLike() {
        var userExample = new User();
        userExample.setName(USER_NAME);

        Example<User> exampleTerm = Example.of(userExample,
                ExampleMatcher.matching().withIgnoreNullValues()
                        .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains()
                                .ignoreCase()));

        List<User> expectedUsers = userRepository.findAll(exampleTerm);
        assertThat(expectedUsers).allMatch(user -> user.getName().contains(USER_NAME));
    }
}
