package org.eclipse.vorto.repository.web.account.dto;

import java.sql.Timestamp;

import org.eclipse.vorto.repository.account.impl.User;

public class UserDto {
	private String username;

	private String role;

	private Timestamp dateCreated;

	private Timestamp lastUpdated;

	public static UserDto fromUser(User user) {
		return new UserDto(user.getUsername(), user.getRole().toString(), user.getDateCreated(), user.getLastUpdated());
	}

	public UserDto() {
		super();
	}

	private UserDto(String username, String role, Timestamp dateCreated,
			Timestamp lastUpdated) {
		this.username = username;
		this.role = role;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
	}

	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
