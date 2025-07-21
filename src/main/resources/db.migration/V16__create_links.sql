CREATE TABLE activity_links (
    activity_id BIGINT NOT NULL,
    link VARCHAR(255),
    description VARCHAR(255)
);

ALTER TABLE activity_links
    ADD CONSTRAINT fk_activity_links_activity
        FOREIGN KEY (activity_id)
            REFERENCES activity(id);