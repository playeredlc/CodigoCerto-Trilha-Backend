package app.edlc.taskapi.user.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "username", unique = true)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column
	private Boolean account_non_expired;
	@Column
	private Boolean account_non_locked;
	@Column
	private Boolean credentials_non_expired;
	@Column
	private Boolean enabled;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_permission",
		joinColumns = { @JoinColumn(name = "id_user") },
		inverseJoinColumns = { @JoinColumn(name = "id_permission") }
	)
	private List<Permission> permissions;
	
	public User() {}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();		
		
		for (Permission p : permissions) {
			roles.add(p.getDescription());
		}		
		return roles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissions;
	}	
	@Override
	public String getPassword() {
		return this.password;
	}
	@Override
	public String getUsername() {
		return this.username;
	}
	
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

	public Boolean getAccount_non_expired() {
		return account_non_expired;
	}

	public void setAccount_non_expired(Boolean account_non_expired) {
		this.account_non_expired = account_non_expired;
	}

	public Boolean getAccount_non_locked() {
		return account_non_locked;
	}

	public void setAccount_non_locked(Boolean account_non_locked) {
		this.account_non_locked = account_non_locked;
	}

	public Boolean getCredentials_non_expired() {
		return credentials_non_expired;
	}

	public void setCredentials_non_expired(Boolean credentials_non_expired) {
		this.credentials_non_expired = credentials_non_expired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account_non_expired, account_non_locked, credentials_non_expired, enabled, id, name,
				password, permissions, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(account_non_expired, other.account_non_expired)
				&& Objects.equals(account_non_locked, other.account_non_locked)
				&& Objects.equals(credentials_non_expired, other.credentials_non_expired)
				&& Objects.equals(enabled, other.enabled) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password)
				&& Objects.equals(permissions, other.permissions) && Objects.equals(username, other.username);
	}
}
