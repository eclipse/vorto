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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.eclipse.vorto.repository.account.UserUtils;
import org.hibernate.annotations.NaturalId;

import com.google.common.collect.Sets;

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

  // TODO remove
  @Deprecated
  @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
  private Set<TenantUser> tenantUsers = new HashSet<>();

  private String emailAddress;

  @Deprecated
  @Column(nullable = false)
  private boolean sysadmin;

  public static User create(String username, String provider, String subject) {
    return create(username, provider, subject, false);
  }

  public static User create(String username, String provider, String subject, boolean isTechnicalUser) {
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

  // TODO remove  ops and move to user service

  // TODO remove
  public static User create(String username, String provider, String subject, Tenant tenant, Role... roles) {
    return create(username, provider, subject, false, tenant, roles);
  }

  // TODO remove
  public static User create(String username, String provider, String subject, boolean isTechnicalUser, Tenant tenant, Role... roles) {
    User user = create(username, provider, subject, isTechnicalUser);

    TenantUser tenantUser = new TenantUser();
    tenantUser.addRoles(roles);
    tenantUser.setTenant(tenant);
    tenantUser.setUser(user);

    user.tenantUsers.add(tenantUser);

    return user;
  }

  // TODO remove
  public Set<UserRole> getRoles(String tenantId) {
    return getTenantUser(tenantId).map(TenantUser::getRoles).orElse(Collections.emptySet());
  }

  // TODO: remove
  public void setRoles(String tenantId, Set<UserRole> roles) {
    getTenantUser(tenantId).ifPresent((tenantUser) -> {
      tenantUser.setRoles(roles);
    });
  }

  // TODO remove
  public void addRolesIfExistingTenant(String tenantId, Role... roles) {
    getTenantUser(tenantId).ifPresent((tenantUser) -> {
      tenantUser.addRoles(roles);
    });
  }

  // TODO remove
  public void addRoles(Tenant tenant, Role... roles) {
    Optional<TenantUser> _tenantUser = getTenantUser(tenant.getTenantId());
    if (_tenantUser.isPresent()) {
      _tenantUser.get().addRoles(roles);
    } else {
      TenantUser tenantUser = new TenantUser();
      tenantUser.setRoles(toUserRoles(Arrays.asList(roles)));
      tenantUser.setTenant(tenant);
      tenantUser.setUser(this);
      tenantUsers.add(tenantUser);
    }
  }

  // TODO remove
  public Map<String, Set<Role>> getTenantRoles() {
    return tenantUsers.stream()
        .collect(Collectors.toMap(tenantUser -> tenantUser.getTenant().getTenantId(),
            tenantUser -> toRoles(tenantUser.getRoles())));
  }

  // TODO remove
  public Set<Role> getAllRoles() {
    return getTenantRoles().values().stream().reduce(Sets.newHashSet(),
        (set1, set2) -> Sets.union(set1, set2));
  }

  // TODO remove
  private static Set<Role> toRoles(Collection<UserRole> userRoles) {
    return userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());
  }

  // TODO remove
  private static Set<UserRole> toUserRoles(Collection<Role> roles) {
    return roles.stream().map(role -> {
      UserRole userRole = new UserRole();
      userRole.setRole(role);
      return userRole;
    }).collect(Collectors.toSet());
  }

  // TODO remove
  private Optional<TenantUser> getTenantUser(String tenantId) {
    if (tenantUsers == null || tenantUsers.isEmpty()) {
      return Optional.empty();
    }

    return tenantUsers.stream()
        .filter(user -> user.getTenant() != null && user.getTenant().getTenantId() != null
            && user.getTenant().getTenantId().equals(tenantId))
        .findFirst();
  }

  // TODO remove
  public Set<Role> getUserRoles(String tenantId) {
    return UserUtils.extractRolesAsList(getRoles(tenantId));
  }

  // TODO remove
  public Set<Tenant> getTenants() {
    if (tenantUsers == null || tenantUsers.isEmpty()) {
      return Collections.emptySet();
    }

    return tenantUsers.stream().filter(tu -> tu.getTenant() != null).map(tu -> tu.getTenant())
        .collect(Collectors.toSet());
  }

  // etc.
  public boolean isSysAdmin(String tenantId) {
    return hasRole(tenantId, Role.SYS_ADMIN);
  }

  public boolean isReviewer(String tenantId) {
    return hasRole(tenantId, Role.MODEL_REVIEWER);
  }

  public boolean hasRole(String tenantId, Role role) {
    Set<Role> roles = getUserRoles(tenantId);
    return roles.stream().anyMatch(e -> e == role);
  }

  @PreRemove
  private void removeTenantUserAndOwnedTenants() {
    Iterator<TenantUser> itTenantUsers = tenantUsers.iterator();
    while(itTenantUsers.hasNext()) {
      TenantUser tenantUser = itTenantUsers.next();
      itTenantUsers.remove();
      tenantUser.setTenant(null);
      tenantUser.setUser(null);
    }
  }

  public boolean isSysadmin() {
    return sysadmin;
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

  public Set<TenantUser> getTenantUsers() {
    return tenantUsers;
  }

  public void setTenantUsers(Set<TenantUser> tenants) {
    this.tenantUsers = tenants;
  }

  public void addTenantUser(TenantUser tenantUser) {
    this.tenantUsers.add(tenantUser);
    tenantUser.setUser(this);
  }

  public void removeTenantUser(TenantUser tenantUser) {
    this.tenantUsers.remove(tenantUser);
    tenantUser.setUser(null);
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }
}
