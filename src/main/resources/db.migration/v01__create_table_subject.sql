CREATE SEQUENCE subject_sequence;

CREATE TABLE subject (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) UNIQUE,
    is_active BOOLEAN NOT NULL
);
