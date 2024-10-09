package app.edlc.taskapi.security.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TokenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private Boolean isAuthenticated;
	private Date createdAt;
	private Date expiration;
	private String accessToken;
	private String refreshToken;
	
	public TokenDto() {}
	
	public TokenDto(String username,
					Boolean isAuthenticated,
					Date createdAt,
					Date expiration,
					String accessToken,
					String refreshToken) {		
		this.username = username;
		this.isAuthenticated = isAuthenticated;
		this.createdAt = createdAt;
		this.expiration = expiration;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	// Getters, Setters, Equals and HashCode
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIsAuthenticated() {
		return isAuthenticated;
	}

	public void setIsAuthenticated(Boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken, createdAt, expiration, isAuthenticated, refreshToken, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDto other = (TokenDto) obj;
		return Objects.equals(accessToken, other.accessToken) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(expiration, other.expiration)
				&& Objects.equals(isAuthenticated, other.isAuthenticated)
				&& Objects.equals(refreshToken, other.refreshToken) && Objects.equals(username, other.username);
	}
}
