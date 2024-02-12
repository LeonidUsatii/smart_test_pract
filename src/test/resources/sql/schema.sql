
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    hash_password VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    level_of_user INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS applicants (
    id IDENTITY PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255),
    phone_number VARCHAR(255),
    hash_password VARCHAR(255) NOT NULL
    );


CREATE TABLE IF NOT EXISTS exam_task (
    id IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL
    );


CREATE TABLE IF NOT EXISTS exam (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    examScore INT,
    examStartTime TIMESTAMP,
    examEndTime TIMESTAMP,
    examDuration INT,
    examStatus VARCHAR(255),
    user_id BIGINT,
    applicant_id BIGINT,
    examtask_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (applicant_id) REFERENCES applicants(id),
    FOREIGN KEY (examtask_id) REFERENCES tasks(id)
    );