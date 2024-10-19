ALTER TABLE users
    ADD CONSTRAINT username_unique_constraint UNIQUE (username);
