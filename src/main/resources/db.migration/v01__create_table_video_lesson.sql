CREATE TABLE video_lesson (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    video_url VARCHAR(200) NOT NULL,
    video_duration INTEGER,
    activity_id BIGINT,
    CONSTRAINT fk_video_lesson_activity
      FOREIGN KEY (activity_id)
          REFERENCES activity(id)
);
