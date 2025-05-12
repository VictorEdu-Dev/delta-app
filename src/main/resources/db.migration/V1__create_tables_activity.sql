CREATE TABLE IF NOT EXISTS subject (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) UNIQUE,
    is_active BOOLEAN NOT NULL,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS activity (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    activity_type VARCHAR(255) NOT NULL,
    image_url VARCHAR(150),
    recommended_level INTEGER NOT NULL,
    max_score NUMERIC(19,4),
    subject_id INTEGER,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_activity_subject
        FOREIGN KEY (subject_id)
            REFERENCES subject(id)
);

CREATE TABLE IF NOT EXISTS video_lesson (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    video_url VARCHAR(200) NOT NULL,
    video_duration INTEGER,
    activity_id BIGINT,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_video_lesson_activity
      FOREIGN KEY (activity_id)
          REFERENCES activity(id)
);