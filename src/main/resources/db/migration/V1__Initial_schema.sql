CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE project
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    status  VARCHAR(20) NOT NULL CHECK (status IN ('TO DO', 'DONE')),
    deleted BOOLEAN DEFAULT false,
    user_id BIGINT REFERENCES users (id)
);

CREATE INDEX user_id_idx ON users (id);

CREATE TABLE task
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(1000),
    due_date    TIMESTAMP,
    priority    VARCHAR(20) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    status      VARCHAR(20) NOT NULL CHECK (status IN ('TO DO', 'DONE')),
    deleted     BOOLEAN DEFAULT false,
    project_id  BIGINT REFERENCES project (id)
);

CREATE INDEX project_id_idx ON project (id);

CREATE TABLE subtask
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(1000),
    status      VARCHAR(20) NOT NULL CHECK (status IN ('TO DO', 'DONE')),
    deleted     BOOLEAN DEFAULT false,
    task_id     BIGINT REFERENCES task (id)
);

CREATE INDEX task_id_idx ON task (id);
