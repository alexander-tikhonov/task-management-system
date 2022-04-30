package com.tikhonov.repositories;

import com.tikhonov.models.Comment;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository for working with comments")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryIntegrationTest {

    private static final String POSTGRESQL_CONTAINER_VERSION = "postgres:14.2";
    private static final String DB_NAME = "task_management_system";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "secret";

    private static final Long TASK_ID = 2L;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(POSTGRESQL_CONTAINER_VERSION)
                    .withDatabaseName(DB_NAME)
                    .withUsername(DB_USER)
                    .withPassword(DB_PASSWORD);

    @TestConfiguration
    static class CommentRepositoryIntegrationTestConfig {

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
    private CommentRepository commentRepository;


    @DisplayName(" should find all comments by task id")
    @Transactional(readOnly = true)
    @Test
    public void shouldFindAllCommentsByTaskId() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();

        List<Comment> expectedComments = commentRepository.findAllByTask_Id(TASK_ID);
        assertThat(expectedComments).isNotNull()
                .allMatch(comment -> Objects.equals(comment.getTask().getId(), TASK_ID));
    }
}
