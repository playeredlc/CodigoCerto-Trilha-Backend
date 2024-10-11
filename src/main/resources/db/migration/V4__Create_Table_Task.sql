CREATE TABLE IF NOT EXISTS task (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    priority TEXT NOT NULL CHECK(priority IN ('low', 'medium', 'high')) DEFAULT 'low',
    status TEXT NOT NULL CHECK(status IN ('pending', 'completed')) DEFAULT 'pending',
    deadline DATETIME,
    id_user INTEGER,
    FOREIGN KEY (id_user) REFERENCES user(id) ON DELETE CASCADE
);
