CREATE TABLE IF NOT EXISTS subject (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS monitor (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    user_monitor_id BIGINT NOT NULL,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS monitoring (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    monitor_id BIGINT,
    description VARCHAR(150),
    url_thumbnail VARCHAR(300),
    location VARCHAR(50),
    subject_id BIGINT,
    vacancies INTEGER NOT NULL DEFAULT 40,
    mode VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS monitoring_day_times (
    entry_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    monitoring_id BIGINT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS day_times (
    entry_id BIGINT NOT NULL,
    time TIMESTAMP,
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_day_times FOREIGN KEY (entry_id) REFERENCES monitoring_day_times(entry_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS monitoring_users (
    monitoring_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (monitoring_id, user_id),
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS subject_activity (
    subject_id BIGINT NOT NULL,
    activity_id BIGINT NOT NULL,
    PRIMARY KEY (subject_id, activity_id),
    version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Foreign keys

ALTER TABLE monitor
    ADD CONSTRAINT fk_user_monitor FOREIGN KEY (user_monitor_id) REFERENCES "user_delta"(id);

ALTER TABLE monitoring
    ADD CONSTRAINT fk_monitor FOREIGN KEY (monitor_id) REFERENCES monitor(id),
    ADD CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subject(id);

ALTER TABLE monitoring_day_times
    ADD CONSTRAINT fk_monitoring_day_times FOREIGN KEY (monitoring_id) REFERENCES monitoring(id) ON DELETE CASCADE;

ALTER TABLE monitoring_users
    ADD CONSTRAINT fk_monitoring FOREIGN KEY (monitoring_id) REFERENCES monitoring(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user_delta"(id);

ALTER TABLE subject_activity
    ADD CONSTRAINT fk_subject_activity_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
    ADD CONSTRAINT fk_subject_activity_activity FOREIGN KEY (activity_id) REFERENCES activity(id);
