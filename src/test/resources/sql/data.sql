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
       