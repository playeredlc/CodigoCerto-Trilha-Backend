CREATE TABLE IF NOT EXISTS user_permission (
  id_user INTEGER NOT NULL,
  id_permission INTEGER NOT NULL,
  PRIMARY KEY (id_user, id_permission),
  FOREIGN KEY (id_user) REFERENCES user (id),
  FOREIGN KEY (id_permission) REFERENCES permission (id)
);
