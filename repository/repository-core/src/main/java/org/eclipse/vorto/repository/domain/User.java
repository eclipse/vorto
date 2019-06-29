/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
public class User {

  public static final String USER_ANONYMOUS = "anonymous";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  @NaturalId
  private String username;

  @Column(nullable = false)
  private Timestamp dateCreated;

  @Column(nullable = false)
  private Timestamp ackOfTermsAndCondTimestamp;

  @Column(nullable = false)
  private Timestamp lastUpdated;

  @OneToMany(orphanRemoval = true, mappedBy = "user")
  private Set<TenantUser> tenantUsers = new HashSet<TenantUser>();

  @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true,
      mappedBy = "owner")
  private Set<Tenant> ownedTenants = new HashSet<Tenant>();

  private String emailAddress;

  public static User create(String username) {
    User user = new User();
    user.setUsername(username);
    user.setDateCreated(new Timestamp(System.currentTimeMillis()));
    user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
    user.setAckOfTermsAndCondTimestamp(new Timestamp(System.currentTimeMillis()));

    return user;
  }

  public static User create(String username, Tenant tenant, Role... roles) {
    User user = create(username);

    TenantUser tenantUser = new TenantUser();
    tenantUser.addRoles(roles);
    tenantUser.setTenant(tenant);
    tenantUser.setUser(user);

    user.tenantUsers.add(tenantUser);

    return user;
  }

  public Set<UserRole> getRoles(String tenantId) {
    return getTenantUser(tenantId).map(TenantUser::getRoles).orElse(Collections.emptySet());
  }

  // TODO: Set better name that describes behavior
  public void setRoles(String tenantId, Set<UserRole> roles) {
    getTenantUser(tenantId).ifPresent((tenantUser) -> {
      tenantUser.setRoles(roles);
    });
  }

  // TODO: Set better name that describes behavior
  public void addRolesIfExistingTenant(String tenantId, Role... roles) {
    getTenantUser(tenantId).ifPresent((tenantUser) -> {
      tenantUser.addRoles(roles);
    });
  }

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

  public Map<String, Set<Role>> getTenantRoles() {
    return tenantUsers.stream()
        .collect(Collectors.toMap(tenantUser -> tenantUser.getTenant().getTenantId(),
            tenantUser -> toRoles(tenantUser.getRoles())));
  }

  public Set<Role> getAllRoles() {
    return getTenantRoles().values().stream().reduce(Sets.newHashSet(),
        (set1, set2) -> Sets.union(set1, set2));
  }

  private static Set<Role> toRoles(Collection<UserRole> userRoles) {
    return userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());
  }

  private static Set<UserRole> toUserRoles(Collection<Role> roles) {
    return roles.stream().map(role -> {
      UserRole userRole = new UserRole();
      userRole.setRole(role);
      return userRole;
    }).collect(Collectors.toSet());
  }

  private Optional<TenantUser> getTenantUser(String tenantId) {
    if (tenantUsers == null || tenantUsers.isEmpty()) {
      return Optional.empty();
    }

    return tenantUsers.stream()
        .filter(user -> user.getTenant() != null && user.getTenant().getTenantId() != null
            && user.getTenant().getTenantId().equals(tenantId))
        .findFirst();
  }

  public Set<Role> getUserRoles(String tenantId) {
    return UserUtils.extractRolesAsList(getRoles(tenantId));
  }

  public Set<Tenant> getTenants() {
    if (tenantUsers == null || tenantUsers.isEmpty()) {
      return Collections.emptySet();
    }

    return tenantUsers.stream().filter(tu -> tu.getTenant() != null).map(tu -> tu.getTenant())
        .collect(Collectors.toSet());
  }

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
    Iterator<Tenant> itOwnedTenants = ownedTenants.iterator();
    while(itOwnedTenants.hasNext()) {
      Tenant tenant = itOwnedTenants.next();
      itOwnedTenants.remove();
      tenant.setOwner(null);
    }
    
    Iterator<TenantUser> itTenantUsers = tenantUsers.iterator();
    while(itTenantUsers.hasNext()) {
      TenantUser tenantUser = itTenantUsers.next();
      itTenantUsers.remove();
      tenantUser.setTenant(null);
      tenantUser.setUser(null);
    }
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

  public boolean hasEmailAddress() {
    return this.emailAddress != null && !"".equals(this.emailAddress);
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

  public Set<Tenant> getOwnedTenants() {
    return ownedTenants;
  }

  public void setOwnedTenants(Set<Tenant> ownedTenants) {
    this.ownedTenants = ownedTenants;
  }

  public void addOwnedTenant(Tenant tenant) {
    this.ownedTenants.add(tenant);
  }

  public void removeOwnedTenant(Tenant tenant) {
    this.ownedTenants.remove(tenant);
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
