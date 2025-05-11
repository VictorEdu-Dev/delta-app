CREATE TABLE activity (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    activity_type VARCHAR(255) NOT NULL,
    image_url VARCHAR(150),
    recommended_level INTEGER NOT NULL,
    max_score NUMERIC(19,4),
    subject_id INTEGER,
    CONSTRAINT fk_activity_subject
        FOREIGN KEY (subject_id)
            REFERENCES subject(id)
);
