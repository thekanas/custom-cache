--liquibase formatted sql
--changeset astolybko:1
CREATE TABLE users
(
    user_id         BIGSERIAL PRIMARY KEY,
    full_name       VARCHAR(60)        NOT NULL,
    passport_number VARCHAR(60) UNIQUE NOT NULL,
    email           VARCHAR(60) UNIQUE,
    password        VARCHAR(60)        NOT NULL
);