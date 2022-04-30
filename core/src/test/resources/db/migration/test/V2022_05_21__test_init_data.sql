INSERT INTO USERS (NAME, EMAIL)
VALUES ('Ivan Ivanov', 'ivanov@mail.ru'),
       ('First User', 'u1@mail.ru'),
       ('Second User', 'u2@mail.ru'),
       ('Ivan Petrov', 'petrov@mail.ru');

INSERT INTO TASKS(TITLE, DESCRIPTION, CREATED_BY, ASSIGNEE_ID, STATUS, PRIORITY)
VALUES ('First task title', 'First task description', 1, 2, 0, 0),
       ('Second task title', 'Second task description', 2, 1, 1, 1),
       ('Third task title', 'Third task description', 3, 2, 2, 2),
       ('Fourth task title', 'Fourth task description', 1, 3, 3, 2),
       ('Fifth task title', 'Fifth task description', 4, 2, 1, 1);

INSERT INTO COMMENTS(CONTENT, CREATED_AT, TASK_ID, USER_ID)
VALUES ('Some comment 1', CURRENT_TIMESTAMP, 1, 2),
       ('Some comment 2', CURRENT_TIMESTAMP, 2, 1),
       ('Some comment 3', CURRENT_TIMESTAMP, 2, 3),
       ('Some comment 4', CURRENT_TIMESTAMP, 3, 3),
       ('Some comment 5', CURRENT_TIMESTAMP, 4, 1);