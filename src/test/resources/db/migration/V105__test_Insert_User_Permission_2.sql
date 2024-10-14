INSERT INTO
	`user_permission` (`id_user`, `id_permission`)
VALUES
	((SELECT `id` FROM `user` WHERE `username` = 'second_tester_username'), 1);