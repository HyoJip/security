-- 사용자 데이터 삽입 (패스워드는 암호화된 값으로 저장해야 합니다)
INSERT INTO users (username, password, enabled) VALUES
    ('testuser', '1234', true);

-- 권한 데이터 삽입
INSERT INTO authorities (username, authority) VALUES
    ('testuser', 'ROLE_USER');
