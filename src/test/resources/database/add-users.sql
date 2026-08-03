INSERT INTO users (id, username, password, email, first_name, last_name, telegram_chat_id)
VALUES (
    1,
    'admin',
    '$2a$10$/x2M6O68D9lExHFBF1ELruLPYnAaVZvy0BzNJ6G9qAnEgypSBLIBa',
    'admin@example.com',
    'Admin',
    'User',
    10001
);

INSERT INTO users (id, username, password, email, first_name, last_name, telegram_chat_id)
VALUES (
    2,
    'assignee',
    '$2a$10$/x2M6O68D9lExHFBF1ELruLPYnAaVZvy0BzNJ6G9qAnEgypSBLIBa',
    'assignee@example.com',
    'Task',
    'Assignee',
    NULL
);

INSERT INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 1);
