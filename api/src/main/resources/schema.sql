CREATE TABLE IF NOT EXISTS lecture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lecturer VARCHAR(100) NOT NULL,
    place VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    time TIMESTAMP NOT NULL,
    content VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

COMMENT ON TABLE lecture IS '강연 정보';

COMMENT ON COLUMN lecture.id IS'id';
COMMENT ON COLUMN lecture.lecturer IS'강연자';
COMMENT ON COLUMN lecture.place IS'강연 장소';
COMMENT ON COLUMN lecture.capacity IS'참여 가능 인원';
COMMENT ON COLUMN lecture.time IS'강연 시간';
COMMENT ON COLUMN lecture.content IS'강연 내용';
COMMENT ON COLUMN lecture.created_at IS'생성 시간';
COMMENT ON COLUMN lecture.updated_at IS'수정 시간';


CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_no INT NOT NULL,
    name VARCHAR(100) NOT NULL
);

COMMENT ON TABLE users IS '사용자 정보';
COMMENT ON COLUMN users.user_no is '사용자 번호';
COMMENT ON COLUMN users.name is '사용자 이름';

CREATE INDEX IF NOT EXISTS idx_users_user_no ON users (user_no);

CREATE TABLE LectureRegistrationLog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    lecture_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL, -- QUEUED, PROCESSING, SUCCESS, FAILED
    message VARCHAR(255),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);