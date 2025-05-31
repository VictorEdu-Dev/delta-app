CREATE TABLE user_delta (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    password_hash VARCHAR(70) NOT NULL,
    created_at TIMESTAMP,
    profile_id BIGINT,
    version TIMESTAMP
);

CREATE TABLE profile (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    profile_image BYTEA,
    phone_number VARCHAR(15),
    total_score NUMERIC(19, 4),
    level INTEGER NOT NULL,
    bio VARCHAR(150),
    user_id BIGINT,
    version TIMESTAMP
);

-- Constraints da user_delta
ALTER TABLE user_delta
    ADD CONSTRAINT uq_user_username UNIQUE (username),
    ADD CONSTRAINT uq_user_email UNIQUE (email),
    ADD CONSTRAINT fk_user_profile FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON DELETE CASCADE;

-- Constraints da profile
ALTER TABLE profile
    ADD CONSTRAINT uq_profile_user UNIQUE (user_id),
    ADD CONSTRAINT uq_profile_phone_number UNIQUE (phone_number),
    ADD CONSTRAINT fk_profile_user FOREIGN KEY (user_id)
        REFERENCES user_delta (id)
        ON DELETE SET NULL;
