INSERT INTO users(username, password)
VALUES ('owner', '$2a$10$vhByBgcu.qCfc6sHMxSZyeGl73INKFN/IE5YFLv5RPNZNk340F7Re');

INSERT INTO project(name, status, user_id)
VALUES ('Project 1', 'TO_DO', 1),
       ('Project 2', 'TO_DO', 1);

INSERT INTO task(name, description, due_date, priority, status, project_id)
VALUES ('Task 1', 'Task for Project 1', '2024-10-30 20:15:33.018000', 'LOW', 'TO_DO', 1),
       ('Task 2', 'Task for Project 2', '2024-10-30 20:15:33.018000', 'LOW', 'TO_DO', 2);

INSERT INTO subtask(name, description, status, task_id)
VALUES ('Subtask 1', 'Subtask for Task 1', 'TO_DO', 1),
       ('Subtask 2', 'Subtask for Task 1', 'TO_DO', 1);