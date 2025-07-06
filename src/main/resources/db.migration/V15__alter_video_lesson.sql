ALTER TABLE video_lesson
    ADD COLUMN IF NOT EXISTS video_id VARCHAR(50);

ALTER TABLE video_lesson ALTER COLUMN video_id SET NOT NULL;
ALTER TABLE video_lesson ADD CONSTRAINT unique_video_id UNIQUE (video_id);
