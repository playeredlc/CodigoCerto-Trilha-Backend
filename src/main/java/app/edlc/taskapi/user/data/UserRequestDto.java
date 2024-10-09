package app.edlc.taskapi.user.data;

import java.io.Serializable;
import java.util.Objects;

public class UserRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String username;
	private String password;
	
	public UserRequestDto() {}
	
	// Getters, Setters, Equals and HashCode
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRequestDto other = (UserRequestDto) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}
}
