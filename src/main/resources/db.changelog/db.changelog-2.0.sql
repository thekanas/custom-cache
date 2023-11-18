--liquibase formatted sql
--changeset astolybko:1
INSERT INTO users(full_name, passport_number, email, password)
VALUES ('Yuriy Mihailovich Pinchuk', '308767HB134', 'email1@email.com', '123'),
       ('Olga Vasilevna Sugak', '308767HB136', 'email2@email.com', '123'),
       ('Yuliya Viktorovna Dolgacheva', '308767HB137', 'email3@email.com', '123'),
       ('Petr Sergeevich Eroshenko', '308767HB138', 'email4@email.com', '123'),
       ('Viktor Gennadevich Stolypin', '308767HB139', 'email5@email.com', '123'),
       ('Yurii Valerevich Pinchuk ', '308767HB120', 'email6@email.com', '123'),
       ('Evgenii Petrovich Bazylev', '308767HB126', 'email7@email.com', '123'),
       ('Yurii Sergeevich Ivanov', '308767HB143', 'email8@email.com', '123'),
       ('Petrova Anna Yurevna', '308767HB156', 'email9@email.com', '123'),
       ('Timofei Aleksandrovich Kot', '308767HB165', 'email10@email.com', '123');