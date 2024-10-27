-- 사용자 테이블
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL
);

-- 권한 테이블
CREATE TABLE authorities (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             CONSTRAINT fk_users_username FOREIGN KEY (username) REFERENCES users (username)
);