CREATE TABLE blacklist (
    token VARCHAR(512) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (token)
);

ALTER TABLE blacklist
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user_delta"(id);
