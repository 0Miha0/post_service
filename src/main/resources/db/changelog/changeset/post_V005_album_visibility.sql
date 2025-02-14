ALTER TABLE album
    ADD COLUMN visibility VARCHAR(20) DEFAULT 'ALL_USERS';

CREATE TABLE album_visibility
(
    album_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    FOREIGN KEY (album_id) REFERENCES album (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    PRIMARY KEY (album_id, user_id)
);