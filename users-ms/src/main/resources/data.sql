
INSERT INTO `users` (user_id, user_name, first_name, last_name, email, enable, password, created_at)
VALUES ('jandchase_system','jandchase_system','jandchase_system', 'jandchase_system', 'first_user@email.test',1,
        '$2a$10$ykhXmCAam5FUEF9GN.4Z8OwwWJidvMii6VFYe77cmS2X6oF6p4W86','2019-06-07');

INSERT INTO `roles` (name, description) VALUES ('ROLE_USER','Role for common users.');
INSERT INTO `roles` (name, description) VALUES ('ROLE_ADMIN', 'Role for common admin users.');

INSERT INTO `users_to_roles` (user_id, role_id) VALUES (1, 1);
INSERT INTO `users_to_roles` (user_id, role_id) VALUES (1, 2);