package app.edlc.taskapi.task.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import app.edlc.taskapi.task.data.enums.Priority;
import app.edlc.taskapi.task.data.enums.Status;
import app.edlc.taskapi.user.data.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "priority", nullable = false)
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "deadline")
	private Date deadline;
	
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	private User user;
	
	public Task() {}
	
	// Getters, Setters, Equals and HashCode

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deadline, description, id, priority, status, title, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(deadline, other.deadline) && Objects.equals(description, other.description)
				&& Objects.equals(id, other.id) && priority == other.priority && status == other.status
				&& Objects.equals(title, other.title) && Objects.equals(user, other.user);
	}
}
