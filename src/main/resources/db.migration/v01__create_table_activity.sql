CREATE TABLE activity (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    activity_type VARCHAR(255) NOT NULL,
    image_url VARCHAR(150),
    recommended_level INTEGER NOT NULL,
    max_score NUMERIC(19,4)
);
