CREATE INDEX idx_secret_user_id ON secret (user_id);
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_refresh_token_user_id ON refresh_token (user_id);
CREATE INDEX idx_token ON refresh_token (token);