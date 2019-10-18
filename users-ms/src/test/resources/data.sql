
INSERT INTO `users` (user_id, user_name, first_name, last_name, email, enable, password, created_at)
VALUES ('FIRST_USER_ID','FIRST_USER','FirstUser', 'FirstUser', 'first_user@email.test',1,'12345678','2019-06-07');

INSERT INTO `users` (user_id, user_name, first_name, last_name, email, enable, password, created_at)
VALUES ('SECOND_USER_ID','SECOND_USER','SecondUser', 'SecondUser', 'second_user@email.test',1,'12345678','2019-06-06');

INSERT INTO `users` (user_id, user_name, first_name, last_name, email, enable, password, created_at)
VALUES ('FIFTH_USER_ID','FIFTH_USER','FifthUser', 'FifthUser', 'fifth_user@email.test',1,'12345678','2019-06-06');

INSERT INTO `roles` (name, description) VALUES ('ROLE_USER','Role for common users.');
INSERT INTO `roles` (name, description) VALUES ('ROLE_ADMIN', 'Role for common admin users.');
INSERT INTO `roles` (name, description) VALUES ('ROLE_TO_DELETE', 'Role for common admin users.');

INSERT INTO `users_to_roles` (user_id, role_id) VALUES (1, 1);
INSERT INTO `users_to_roles` (user_id, role_id) VALUES (2, 2);
INSERT INTO `users_to_roles` (user_id, role_id) VALUES (2, 1);