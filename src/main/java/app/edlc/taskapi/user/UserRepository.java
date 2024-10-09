package app.edlc.taskapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import app.edlc.taskapi.user.data.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);
	User findByUsername(String username);
}
