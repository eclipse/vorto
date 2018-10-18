/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.account.impl;

import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.UserUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@OneToMany( mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<UserRole> roles;

	@Column(nullable = false)
	private Timestamp dateCreated;

	@Column(nullable = false)
	private Timestamp ackOfTermsAndCondTimestamp;

	@Column(nullable = false)
	private Timestamp lastUpdated;

	public User() {

	}

	public void addUserRoles(UserRole userRole){
		if(userRole != null) {
			if(roles == null) {
				roles = new HashSet<>();
			}
			userRole.setUser(this);
			roles.add(userRole);
		}
	}

	public void addRoles(Role role) {
		UserRole userRole = new UserRole();
		userRole.setRole(role.toString());
		addUserRoles(userRole);
	}

	public String getUserRolesAsCommaSeparatedString(){
		List<String> userRoles = UserUtils.extractRolesAsList(this.getRoles());
		StringJoiner roles = new StringJoiner(",");

		for(String userRole : userRoles) {
			roles.add("ROLE_" +userRole);

		}
		return roles.toString();
	}

	public static User create(String username) {
		User user = new User();
		user.username = username;
		return user;
	}
	
	public static User create(String username, Role role) {
		User user = new User();
		user.username = username;
		user.addRoles(role);
		return user;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}
	public boolean isAdmin() {
		List<String> roles = UserUtils.extractRolesAsList(this.getRoles());
		return roles.stream().anyMatch( e -> e.equalsIgnoreCase(Role.ADMIN.toString()));
	}

	public boolean isReviewer() {
		List<String> roles = UserUtils.extractRolesAsList(this.getRoles());
		return roles.stream().anyMatch( e -> e.equalsIgnoreCase(Role.MODEL_REVIEWER.toString()));
	}


	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getAckOfTermsAndCondTimestamp() {
		return ackOfTermsAndCondTimestamp;
	}

	public void setAckOfTermsAndCondTimestamp(Timestamp ackOfTermsAndCondTimestamp) {
		this.ackOfTermsAndCondTimestamp = ackOfTermsAndCondTimestamp;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", roles=" + roles +
				", dateCreated=" + dateCreated +
				", ackOfTermsAndCondTimestamp=" + ackOfTermsAndCondTimestamp +
				", lastUpdated=" + lastUpdated +
				'}';
	}


}