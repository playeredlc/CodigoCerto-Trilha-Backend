CREATE TABLE IF NOT EXISTS `user` (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    account_non_expired BOOLEAN DEFAULT true,
	account_non_locked BOOLEAN DEFAULT true,
    credentials_non_expired BOOLEAN DEFAULT true,
    enabled BOOLEAN DEFAULT true
);
