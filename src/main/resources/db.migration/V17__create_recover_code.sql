CREATE TABLE IF NOT EXISTS recovery_codes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(6) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(320) NOT NULL,
    expiration TIMESTAMPTZ NOT NULL,
    reason VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT code_format CHECK (code ~ '^[0-9]{6}$')
);