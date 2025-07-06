ALTER TABLE monitor
    ADD CONSTRAINT unique_user_monitor UNIQUE (user_monitor_id);