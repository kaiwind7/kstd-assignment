INSERT INTO lecture (lecturer, place, capacity, time, content)
VALUES ('John Doe', 'Room 101', 50, '2024-10-01 10:00:00', 'Introduction to Java Programming'),
       ('Jane Smith', 'Room 202', 30, '2024-10-05 14:00:00', 'Advanced Spring Boot Techniques'),
       ('Alice Johnson', 'Main Hall', 100, '2024-10-10 09:00:00', 'Microservices Architecture and Design Patterns'),
       ('Bob Lee', 'Room 303', 25, '2024-10-12 11:00:00', 'Database Optimization and Best Practices'),
       ('Charlie Brown', 'Conference Center', 200, '2024-10-15 13:00:00', 'Cloud Computing with AWS'),
       ('Emma Davis', 'Room 404', 40, '2024-10-20 16:00:00', 'RESTful API Design and Implementation'),
       ('Oliver Wilson', 'Room 505', 35, '2024-10-25 15:00:00', 'Kubernetes and Container Orchestration'),
       ('Sophia Miller', 'Room 606', 45, '2024-10-28 12:00:00', 'Effective DevOps Practices'),
       ('Liam Martinez', 'Room 707', 60, '2024-11-02 09:00:00', 'Machine Learning Fundamentals'),
       ('Amelia Taylor', 'Room 808', 55, '2024-11-05 14:30:00', 'Data Science with Python');


insert into users (user_no, name)
values ('10000', 'A'),
       ('10001', 'B'),
       ('10002', 'C'),
       ('10003', 'D'),
       ('10004', 'E'),
       ('10005', 'F'),
       ('10006', 'G'),
       ('10007', 'H'),
       ('10008', 'J'),
       ('10009', 'K'),
       ('10010', 'L'),
       ('10011', 'M'),
       ('10012', 'N'),
       ('10013', 'O'),
       ('10014', 'P'),
       ('10015', 'Q'),
       ('10016', 'R'),
       ('10017', 'S'),
       ('10018', 'T'),
       ('10019', 'U'),
       ('10020', 'V'),
       ('10021', 'W'),
       ('10022', 'X'),
       ('10023', 'Y'),
       ('10024', 'Z');


insert into LECTURE_REGISTRATION (lecture_id, registration_time, user_id, status)
values (1, NOW(), 1, 'CONFIRMED'),
       (1, NOW(), 2, 'CONFIRMED'),
       (1, NOW(), 3, 'CONFIRMED'),
       (1, NOW(), 4, 'CONFIRMED'),
       (1, NOW(), 5, 'CONFIRMED'),
       (1, NOW(), 6, 'CONFIRMED'),
       (1, NOW(), 7, 'CONFIRMED'),
       (1, NOW(), 8, 'CONFIRMED'),
       (1, NOW(), 9, 'CONFIRMED'),
       (1, NOW(), 10, 'CONFIRMED'),
       (1, NOW(), 11, 'CONFIRMED'),
       (1, NOW(), 12, 'CONFIRMED'),
       (1, NOW(), 13, 'CONFIRMED'),
       (1, NOW(), 14, 'CONFIRMED'),
       (1, NOW(), 15, 'CONFIRMED'),
       (1, NOW(), 16, 'CONFIRMED'),
       (1, NOW(), 17, 'CONFIRMED'),
       (1, NOW(), 18, 'CONFIRMED'),
       (1, NOW(), 19, 'CONFIRMED'),
       (1, NOW(), 20, 'CONFIRMED'),
       (1, NOW(), 21, 'CONFIRMED'),
       (2, NOW(), 1, 'CONFIRMED'),
       (2, NOW(), 2, 'CONFIRMED'),
       (2, NOW(), 3, 'CONFIRMED'),
       (2, NOW(), 4, 'CONFIRMED'),
       (2, NOW(), 5, 'CONFIRMED'),
       (2, NOW(), 6, 'CONFIRMED'),
       (2, NOW(), 7, 'CONFIRMED'),
       (2, NOW(), 8, 'CONFIRMED'),
       (2, NOW(), 9, 'CONFIRMED');