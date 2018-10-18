package org.eclipse.vorto.repository.web.account.dto;

import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.account.impl.UserRole;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDto {
	private String username;

	private List<String> roles;

	private Timestamp dateCreated;

	private Timestamp lastUpdated;

	public static UserDto fromUser(User user) {
		return new UserDto(user.getUsername(), user.getDateCreated(), user.getLastUpdated());
	}

	public static UserDto fromUser(User user, List<UserRole> userRoles) {
		UserDto userDto = fromUser(user);
		userDto.addRoles(userRoles);
		return userDto;
	}

	public UserDto() {
		super();
	}

	private UserDto(String username, Timestamp dateCreated,
			Timestamp lastUpdated) {
		this.username = username;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void addRoles(List<UserRole> userRoles) {
		userRoles.forEach( e -> {
			if(roles == null) {
				roles = new ArrayList<>();
			}
			roles.add( e.getRole());
		});
	}
	public String getUsername() {
		return username;
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

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
