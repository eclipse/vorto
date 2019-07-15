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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import org.eclipse.vorto.model.ModelId;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "tenant")
public class Tenant {

  public static final String STANDARDIZATION_TENANT_ID = "standardization";
  public static final String STANDARDIZATION_TENANT_DEFAULT_NAMESPACE = "org.eclipse.vorto";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  private String tenantId;

  @Column(name = "default_namespace")
  private String defaultNamespace;

  @Column(name = "authentication_provider")
  @Enumerated(EnumType.STRING)
  private AuthenticationProvider authenticationProvider;

  @Column(name = "authorization_provider")
  @Enumerated(EnumType.STRING)
  private AuthorizationProvider authorizationProvider;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tenant")
  private Set<Namespace> namespaces = new HashSet<Namespace>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tenant")
  private Set<TenantUser> users = new HashSet<TenantUser>();

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  public static Tenant newTenant(String tenantId, String defaultNamespace, Set<String> namespaces) {
    Tenant tenant = new Tenant(tenantId);
    tenant.setDefaultNamespace(defaultNamespace);
    tenant.getNamespaces().addAll(Namespace.toNamespace(namespaces, tenant));

    return tenant;
  }

  public Tenant() {}

  public Tenant(String tenantId) {
    this.tenantId = tenantId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Collection<Namespace> getNamespaces() {
    return namespaces;
  }

  public void setNamespaces(Set<Namespace> namespaces) {
    this.namespaces = namespaces;
  }

  public void addNamespace(Namespace namespace) {
    this.namespaces.add(namespace);
    namespace.setTenant(this);
  }

  public void removeNamespace(Namespace ns) {
    this.namespaces.remove(ns);
    ns.setTenant(null);
  }
  
  public void removeNamespace(String ns) {
    if (namespaces != null) {
      Iterator<Namespace> iter = namespaces.iterator();
      while (iter.hasNext()) {
        Namespace namespace = iter.next();
        if (namespace.getName().equals(ns)) {
          namespace.setTenant(null);
          iter.remove();
        }
      }
    }
  }

  private void removeAllNamespaces() {
    if (namespaces != null) {
      Iterator<Namespace> iter = namespaces.iterator();
      while (iter.hasNext()) {
        iter.next();
        iter.remove();
      }
    }
  }
  
  public boolean hasNamespace(String namespace) {
    return this.namespaces.stream().map(Namespace::getName).anyMatch(namespace::equals);
  }

  public Set<User> getTenantAdmins() {
    return users.stream().filter(user -> user.hasRole(Role.TENANT_ADMIN))
        .map(user -> user.getUser()).collect(Collectors.toSet());
  }

  public boolean hasUser(String username) {
    return users.stream().anyMatch(user -> user.getUser().getUsername().equals(username));
  }

  public boolean hasTenantAdmin(String username) {
    return users.stream().anyMatch(
        user -> user.getUser().getUsername().equals(username) && user.hasRole(Role.TENANT_ADMIN));
  }

  public Set<TenantUser> getTenantUserAdmins() {
    return users.stream().filter(user -> user.hasRole(Role.TENANT_ADMIN))
        .collect(Collectors.toSet());
  }

  public Optional<TenantUser> getUser(String userId) {
    return users.stream().filter(user -> user.getUser().getUsername().equals(userId)).findFirst();
  }

  public Set<TenantUser> getUsers() {
    return users;
  }

  public void setUsers(Set<TenantUser> users) {
    this.users = users;
  }

  public void addUser(TenantUser user) {
    users.add(user);
    user.setTenant(this);
  }

  public void removeUser(TenantUser user) {
    users.remove(user);
    user.getUser().removeTenantUser(user);
    user.setTenant(null);
  }

  private void removeUsers() {
    if (users != null) {
      Iterator<TenantUser> iter = users.iterator();
      while (iter.hasNext()) {
        TenantUser user = iter.next();

        user.getUser().removeTenantUser(user);
        user.setTenant(null);
        iter.remove();
      }
    }
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
    owner.addOwnedTenant(this);
  }
  
  private void unsetOwner() {
    owner.removeOwnedTenant(this);
    this.owner = null;
  }
  
  @PreRemove
  private void removeRelationships() {
    removeAllNamespaces();
    removeUsers();
    unsetOwner();
  }

  public String getDefaultNamespace() {
    return defaultNamespace;
  }

  public void setDefaultNamespace(String defaultNamespace) {
    this.defaultNamespace = defaultNamespace;
  }

  public AuthenticationProvider getAuthenticationProvider() {
    return authenticationProvider;
  }

  public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  public AuthorizationProvider getAuthorizationProvider() {
    return authorizationProvider;
  }

  public void setAuthorizationProvider(AuthorizationProvider authorizationProvider) {
    this.authorizationProvider = authorizationProvider;
  }
  
  public boolean owns(ModelId modelId) {
    return getNamespaces() != null && getNamespaces().stream().anyMatch(ns -> ns.owns(modelId));
  }
  
  public boolean owns(String namespace) {
    return getNamespaces().stream().anyMatch(ns -> ns.owns(namespace));
  }

  @Override
  public String toString() {
    return "Tenant [id=" + id + ", tenantId=" + tenantId + ", defaultNamespace=" + defaultNamespace
        + ", authenticationProvider=" + authenticationProvider + ", authorizationProvider="
        + authorizationProvider + ", namespaces=" + toString(namespaces) + ", users=" + users
        + "]";
  }

  private String toString(Set<Namespace> namespaces) {
    return String.join(",",
        namespaces.stream().map(ns -> ns.getName()).collect(Collectors.toList()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
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
    Tenant other = (Tenant) obj;
    if (tenantId == null) {
      if (other.tenantId != null)
        return false;
    } else if (!tenantId.equals(other.tenantId))
      return false;
    return true;
  }
}
