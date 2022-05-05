package com.tikhonov.repositories;

import com.tikhonov.models.Task;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository for working with tasks")
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryIntegrationTest {

    private static final String POSTGRESQL_CONTAINER_VERSION = "postgres:14.2";
    private static final String DB_NAME = "task_management_system";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "secret";

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(POSTGRESQL_CONTAINER_VERSION)
                    .withDatabaseName(DB_NAME)
                    .withUsername(DB_USER)
                    .withPassword(DB_PASSWORD);
    public static final long ASSIGNEE_ID = 1L;
    public static final long CREATED_BY_ID = 2L;

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
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" should find all tasks by assignee id")
    @Test
    public void shouldFindAllTasksByAssigneeId() {
        List<Task> tasks = taskRepository.findAllByAssignee_Id(ASSIGNEE_ID, Sort.unsorted());
        List<Task> expectedTasks = em.getEntityManager()
                .createQuery("SELECT t FROM Task t WHERE t.assignee.id=:id", Task.class)
                .setParameter("id", ASSIGNEE_ID)
                .getResultList();

        assertThat(tasks).containsOnlyOnceElementsOf(expectedTasks);
    }

    @DisplayName(" should find all tasks by createdBy id")
    @Test
    public void shouldFindAllTasksByCreatedById() {
        List<Task> tasks = taskRepository.findAllByCreatedBy_Id(CREATED_BY_ID, Sort.unsorted());
        List<Task> expectedTasks = em.getEntityManager()
                .createQuery("SELECT t FROM Task t WHERE t.createdBy.id=:id", Task.class)
                .setParameter("id", CREATED_BY_ID)
                .getResultList();

        assertThat(tasks).containsOnlyOnceElementsOf(expectedTasks);
    }
}
