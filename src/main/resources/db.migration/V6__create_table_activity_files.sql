CREATE TABLE activity_files (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(200) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    activity_id BIGINT NOT NULL,
    CONSTRAINT fk_activity
        FOREIGN KEY(activity_id)
            REFERENCES activity(id)
            ON DELETE CASCADE
);
