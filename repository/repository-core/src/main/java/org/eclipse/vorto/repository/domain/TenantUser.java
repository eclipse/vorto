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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tenant_user")
public class TenantUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name="tenant_id")
  private Tenant tenant;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER,
      orphanRemoval = true)
  private Set<UserRole> roles;

  public static TenantUser createTenantUser(Tenant tenant, User user, Role ... roles) {
    TenantUser tenantUser = createTenantUser(user, roles);
    tenant.addUser(tenantUser);
    return tenantUser;
  }
  
  public static TenantUser createTenantUser(User user, Role ... roles) {
    TenantUser tenantUser = new TenantUser();
    user.addTenantUser(tenantUser);
    tenantUser.addRoles(roles);
    return tenantUser;
  }
  
  public static TenantUser createTenantUser(Tenant tenant, Role ... roles) {
    TenantUser tenantUser = new TenantUser();
    tenantUser.addRoles(roles);
    tenant.addUser(tenantUser);
    return tenantUser;
  }
  
  public Long getId() {
    return id;
  }
  
  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    if (tenant != null) {
      tenant.getUsers().add(this);
    }
    
    this.tenant = tenant;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Set<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<UserRole> roles) {
    roles.forEach((role) -> role.setUser(this));
    this.roles = roles;
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

  public boolean hasRole(Role role) {
    return getRoles().stream().anyMatch(userRole -> userRole.getRole().equals(role));
  }
  
  public void removeRole(UserRole role) {
    roles.remove(role);
    role.setUser(null);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
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
    TenantUser other = (TenantUser) obj;
    if (tenant == null) {
      if (other.tenant != null)
        return false;
    } else if (!tenant.equals(other.tenant))
      return false;
    if (user == null) {
      if (other.user != null)
        return false;
    } else if (!user.equals(other.user))
      return false;
    return true;
  }
}
