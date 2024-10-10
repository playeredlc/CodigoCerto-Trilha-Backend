INSERT INTO
	`user_permission` (`id_user`, `id_permission`)
VALUES
	((SELECT `id` FROM `user` WHERE `username` = 'tester_username'), 1);