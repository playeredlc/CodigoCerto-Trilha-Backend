package app.edlc.taskapi.task.integrationtests.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class Task implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private String description;
	private Status status;
	private Date dealine;
	
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDealine() {
		return dealine;
	}

	public void setDealine(Date dealine) {
		this.dealine = dealine;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dealine, description, id, status, title);
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
		return Objects.equals(dealine, other.dealine) && Objects.equals(description, other.description)
				&& Objects.equals(id, other.id) && status == other.status && Objects.equals(title, other.title);
	}
}
