package app.edlc.taskapi.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.edlc.taskapi.task.data.Task;
import app.edlc.taskapi.user.data.User;


public interface TaskRepository extends JpaRepository<Task, Long> {
	public List<Task> findByUser(User user);
}
