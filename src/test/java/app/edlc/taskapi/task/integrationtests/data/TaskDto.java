package app.edlc.taskapi.task.integrationtests.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TaskDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private String description;
	private String priority;
	private String status;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date deadline;
	
	public TaskDto() {}
	
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
		return Objects.hash(deadline, description, id, priority, status, title);
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
				&& Objects.equals(id, other.id) && Objects.equals(priority, other.priority)
				&& Objects.equals(status, other.status) && Objects.equals(title, other.title);
	}	
}
