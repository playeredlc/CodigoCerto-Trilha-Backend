package app.edlc.taskapi.task.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TaskDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long key;
	private String title;
	private String description;
	private String priority;
	private String status;
	private Date deadline;
	
	public TaskDto() {}
	
	// Getters, Setters, Equals and HashCode

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deadline, description, key, priority, status, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskDto other = (TaskDto) obj;
		return Objects.equals(deadline, other.deadline) && Objects.equals(description, other.description)
				&& Objects.equals(key, other.key) && Objects.equals(priority, other.priority)
				&& Objects.equals(status, other.status) && Objects.equals(title, other.title);
	}	
}
