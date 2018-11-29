/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.account;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER,
      orphanRemoval = true)
  private Set<UserRole> roles;

  @Column(nullable = false)
  private Timestamp dateCreated;

  @Column(nullable = false)
  private Timestamp ackOfTermsAndCondTimestamp;

  @Column(nullable = false)
  private Timestamp lastUpdated;

  private String emailAddress;

  public User() {

  }

  public void addUserRoles(UserRole userRole) {
    if (userRole != null) {
      if (roles == null) {
        roles = new HashSet<>();
      }
      userRole.setUser(this);
      roles.add(userRole);
    }
  }

  public void addRoles(Role... roles) {
    for (Role role : roles) {
      UserRole userRole = new UserRole();
      userRole.setRole(role);
      addUserRoles(userRole);
    }
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

  public Set<Role> getUserRoles() {
    return UserUtils.extractRolesAsList(this.roles);
  }

  public boolean isAdmin() {
    Set<Role> roles = UserUtils.extractRolesAsList(this.getRoles());
    return roles.stream().anyMatch(e -> e == Role.ADMIN);
  }

  public boolean isReviewer() {
    Set<Role> roles = UserUtils.extractRolesAsList(this.getRoles());
    return roles.stream().anyMatch(e -> e == Role.MODEL_REVIEWER);
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

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", username='" + username + '\'' + ", dateCreated=" + dateCreated
        + ", ackOfTermsAndCondTimestamp=" + ackOfTermsAndCondTimestamp + ", lastUpdated="
        + lastUpdated + '}';
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((ackOfTermsAndCondTimestamp == null) ? 0 : ackOfTermsAndCondTimestamp.hashCode());
    result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
    result = prime * result + ((roles == null) ? 0 : roles.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
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
    if (ackOfTermsAndCondTimestamp == null) {
      if (other.ackOfTermsAndCondTimestamp != null)
        return false;
    } else if (!ackOfTermsAndCondTimestamp.equals(other.ackOfTermsAndCondTimestamp))
      return false;
    if (dateCreated == null) {
      if (other.dateCreated != null)
        return false;
    } else if (!dateCreated.equals(other.dateCreated))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (lastUpdated == null) {
      if (other.lastUpdated != null)
        return false;
    } else if (!lastUpdated.equals(other.lastUpdated))
      return false;
    if (roles == null) {
      if (other.roles != null)
        return false;
    } else if (!roles.equals(other.roles))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

  public boolean hasEmailAddress() {
    return this.emailAddress != null && !"".equals(this.emailAddress);
  }



}
