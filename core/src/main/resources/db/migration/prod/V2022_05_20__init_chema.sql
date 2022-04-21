CREATE TABLE IF NOT EXISTS USERS
(
    ID    BIGSERIAL PRIMARY KEY,
    NAME  VARCHAR(15) NOT NULL,
    EMAIL VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS TASKS
(
    ID          BIGSERIAL PRIMARY KEY,
    TITLE       VARCHAR(255) NOT NULL,
    DESCRIPTION TEXT         NOT NULL,
    CREATED_BY  BIGINT       NOT NULL,
    ASSIGNEE    BIGINT       NOT NULL,
    STATUS      VARCHAR(15)  NOT NULL,
    PRIORITY    VARCHAR(15)  NOT NULL,
    CONSTRAINT fk_created_by FOREIGN KEY (CREATED_BY)
        REFERENCES USERS (ID),
    CONSTRAINT fk_assignee FOREIGN KEY (ASSIGNEE)
        REFERENCES USERS (ID)
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    ID BIGSERIAL PRIMARY KEY,
    CONTENT VARCHAR(500) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL,
    TASK_ID BIGINT NOT NULL,
    USER_ID BIGINT NOT NULL,
    CONSTRAINT fk_task_id FOREIGN KEY (TASK_ID)
        REFERENCES TASKS (ID),
    CONSTRAINT fk_user_id FOREIGN KEY (USER_ID)
        REFERENCES USERS (ID)
);