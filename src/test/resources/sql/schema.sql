
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    hash_password VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    level_of_user INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS applicant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255),
    phone_number VARCHAR(255)
    );


CREATE TABLE IF NOT EXISTS exam_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_task_title VARCHAR(255) NOT NULL
    );


CREATE TABLE IF NOT EXISTS exam (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_score INT,
    exam_start_time TIMESTAMP,
    exam_end_time TIMESTAMP,
    exam_duration INT,
    exam_status VARCHAR(255),
    user_id BIGINT,
    applicant_id BIGINT,
    examtask_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (applicant_id) REFERENCES applicants(id),
    FOREIGN KEY (exam_task_id) REFERENCES exam_task(id)
    );

CREATE TABLE tokens (
    code VARCHAR(255) NOT NULL,
    expired_date_time TIMESTAMP NOT NULL,
    applicant_id BIGINT NOT NULL,
    exam_id BIGINT,
    PRIMARY KEY (code),
    FOREIGN KEY (applicant_id) REFERENCES applicants(id),
    FOREIGN KEY (exam_id) REFERENCES exam(id)
);

