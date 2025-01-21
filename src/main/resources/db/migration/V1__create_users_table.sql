CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    telegram_id BIGINT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    username VARCHAR(100),
    language_code VARCHAR(10),
    chat_id BIGINT NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);