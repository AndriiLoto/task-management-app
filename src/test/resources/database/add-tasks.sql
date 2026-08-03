INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (
    1,
    'Implement API tests',
    'Cover controllers with MockMvc',
    'HIGH',
    'IN_PROGRESS',
    '2026-08-10 12:00:00',
    1,
    2
);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (
    2,
    'Write repository tests',
    'Cover specification builders',
    'MEDIUM',
    'NOT_STARTED',
    '2026-08-20 12:00:00',
    1,
    2
);
