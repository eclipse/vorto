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

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.eclipse.vorto.repository.account.Role;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false)
	private Timestamp dateCreated;

	@Column(nullable = false)
	private Timestamp ackOfTermsAndCondTimestamp;

	@Column(nullable = false)
	private Timestamp lastUpdated;

	public User() {

	}

	public static User create(String username) {
		User user = new User();
		user.username = username;
		user.role = Role.USER;
		return user;
	}
	
	public static User create(String username, Role role) {
		User user = new User();
		user.username = username;
		user.role = role;
		return user;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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
		return "User [id=" + id + ", username=" + username + ", role=" + role + ", dateCreated=" + dateCreated
				+ ", ackOfTermsAndCondTimestamp=" + ackOfTermsAndCondTimestamp + ", lastUpdated=" + lastUpdated + "]";
	}

	public boolean isAdmin() {
		return this.role == Role.ADMIN;
	}
}