ALTER TABLE project
    DROP CONSTRAINT project_status_check;
ALTER TABLE project
    ADD CONSTRAINT project_status_check CHECK (status IN ('TO_DO', 'DONE'));

ALTER TABLE task
    DROP CONSTRAINT task_status_check;
ALTER TABLE task
    ADD CONSTRAINT task_status_check CHECK (status IN ('TO_DO', 'DONE'));

ALTER TABLE subtask
    DROP CONSTRAINT subtask_status_check;
ALTER TABLE subtask
    ADD CONSTRAINT subtask_status_check CHECK (status IN ('TO_DO', 'DONE'));