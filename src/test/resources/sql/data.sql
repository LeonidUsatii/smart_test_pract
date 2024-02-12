DELETE FROM exam;
DELETE FROM users;
DELETE FROM applicants;
DELETE FROM exam_task;

INSERT INTO users (id, first_name, last_name, email, hash_password, user_role, level_of_user)
VALUES (1, 'Ivan', 'Testov', 'ivan.testov@example.com', 'hashedPassword', 'USER', 1),
       (2, 'Lidiya', 'Testova', 'lidiya.testova@example.com', 'hashedPassword', 'USER', 2);

INSERT INTO applicants (first_name, last_name, email, address, phone_number, hash_password)
VALUES ('John', 'Doe', 'john.doe@example.com', '1234 Main St', '123-456-7890', 'hashed_password_1'),
       ('Jane', 'Doe', 'jane.doe@example.com', '5678 Market St', '098-765-4321', 'hashed_password_2');

INSERT INTO exam_task (id, exam_task_title)
VALUES (1, 'Final Exam'), (2, 'Arrays'), (3, 'List'), (4,'Cycles');

INSERT INTO exam (exam_score, exam_start_time, exam_end_time, exam_duration, exam_status, applicant_id, examtask_id, user_id)
VALUES (80, '2023-01-01 10:00:00', '2023-01-01 11:30:00', 70, 'PLANNED', 1, 1, 1),
       (85, '2023-01-01 10:00:00', '2023-01-01 11:30:00', 90, 'PLANNED', 2, 2, 2);





