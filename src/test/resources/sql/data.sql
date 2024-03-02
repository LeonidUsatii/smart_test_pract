insert into test_type(name) values ('TestType');

insert into question(question_text,level,test_type_id)
values ('What is a?',1,1),
       ('What is c?',1,1),
       ('What is d?',1,1),
       ('What is e?',1,1),
       ('What is f?',1,1),
       ('What is g?',1,1);

insert into answer(answer_text,is_correct,question_id)
values ('answer1',true,1),
       ('answer1',false,1),
       ('answer1',false,1),
       ('answer1',false,1),

       ('answer1',true,2),
       ('answer2',false,2),
       ('answer3',false,2),
       ('answer4',false,2),

       ('answer1',true,3),
       ('answer2',false,3),
       ('answer3',false,3),
       ('answer4',false,3),

       ('answer1',true,4),
       ('answer2',false,4),
       ('answer3',false,4),
       ('answer4',false,4),

       ('answer1',true,5),
       ('answer2',false,5),
       ('answer3',false,5),
       ('answer4',false,5),

       ('answer1',true,6),
       ('answer2',false,6),
       ('answer3',false,6),
       ('answer4',false,6);

INSERT INTO users (first_name, last_name, email, hash_password, user_role, level_of_user)
VALUES ('Ivan', 'Testov', 'ivan.testov@example.com', 'hashedPassword', 'USER', 1),
       ('Lidiya', 'Testova', 'lidiya.testova@example.com', 'hashedPassword', 'USER', 2);

INSERT INTO applicants (first_name, last_name, email, address, phone_number)
VALUES ('John', 'Doe', 'john.doe@example.com', '1234 Main St', '123-456-7890'),
       ('Jane', 'Doe', 'jane.doe@example.com', '5678 Market St', '098-765-4321');

INSERT INTO exam_task (exam_task_title)
VALUES ('Final Exam'), ('Arrays'), ('List');

INSERT INTO exam (exam_score, exam_start_time, exam_end_time, exam_duration, exam_status, user_id, applicant_id, examtask_id)
VALUES (80, '2023-01-01 10:00:00', '2023-01-01 11:30:00', 70, 'PLANNED', 1, 1, 1),
       (85, '2023-01-01 10:00:00', '2023-01-01 11:30:00', 90, 'PLANNED', 1, 1, 2);

-- INSERT INTO token (code, expired_date_time, applicant_id, exam_id)
-- VALUES ('qwerty', '2023-03-03 10:00:00', 1, 1),
--        ('qwerty!', '2023-03-03 10:00:00', 2, 2);

       