-- 기존 DB에 새 컬럼/테이블만 추가할 때 실행 (이미 있으면 무시)

-- 컬럼이 이미 있으면 에러 나므로 한 번만 실행
ALTER TABLE tbl_member ADD COLUMN profile_image_path VARCHAR(500);
ALTER TABLE tbl_member ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';
ALTER TABLE tbl_board ADD COLUMN fixed TINYINT DEFAULT 0;
ALTER TABLE tbl_mother_board ADD COLUMN fixed TINYINT DEFAULT 0;
ALTER TABLE tbl_student_board ADD COLUMN fixed TINYINT DEFAULT 0;

-- 아래 테이블들은 schema.sql 에서 CREATE TABLE IF NOT EXISTS 로 생성됨
-- tbl_refresh_token, tbl_password_reset, tbl_point_log, tbl_token_blacklist
