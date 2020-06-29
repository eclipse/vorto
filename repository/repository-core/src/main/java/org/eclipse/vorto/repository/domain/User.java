/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "user")
public class User implements Serializable {

  public static final String USER_ANONYMOUS = "anonymous";
  private static final long serialVersionUID = 5561604486210150801L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  @NaturalId
  private String username;

  @Column(name = "authentication_provider_id")
  private String authenticationProviderId;

  @Column(name = "subject")
  private String subject;

  @Column(name = "is_technical_user")
  private boolean isTechnicalUser;

  @Column(nullable = false)
  private Timestamp dateCreated;

  @Column(nullable = false)
  private Timestamp ackOfTermsAndCondTimestamp;

  @Column(nullable = false)
  private Timestamp lastUpdated;

  private String emailAddress;

  public static User create(String username, String provider, String subject) {
    return create(username, provider, subject, false);
  }

  public static User create(String username, String provider, String subject,
      boolean isTechnicalUser) {
    User user = new User();
    user.setUsername(username);
    user.setAuthenticationProviderId(provider);
    user.setSubject(subject);
    user.setTechnicalUser(isTechnicalUser);
    user.setDateCreated(new Timestamp(System.currentTimeMillis()));
    user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
    user.setAckOfTermsAndCondTimestamp(new Timestamp(System.currentTimeMillis()));

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

  public String getName() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAuthenticationProviderId() {
    return authenticationProviderId;
  }

  public void setAuthenticationProviderId(String authenticationProvider) {
    this.authenticationProviderId = authenticationProvider;
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

  public boolean hasEmailAddress() {
    return this.emailAddress != null && !"".equals(this.emailAddress);
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public boolean isTechnicalUser() {
    return isTechnicalUser;
  }

  public void setTechnicalUser(boolean isTechnicalUser) {
    this.isTechnicalUser = isTechnicalUser;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (username == null) {
      if (other.username != null) {
        return false;
      }
    } else if (!username.equals(other.username)) {
      return false;
    }
    return true;
  }
}
