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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "tenant")
public class Tenant {

  public static final String STANDARDIZATION_TENANT_ID = "standardization";
  public static final String STANDARDIZATION_TENANT_DEFAULT_NAMESPACE = "org.eclipse.vorto";

  public static Tenant newTenant(String tenantId, String defaultNamespace, Set<String> namespaces) {
    Tenant tenant = new Tenant(tenantId);
    tenant.setDefaultNamespace(defaultNamespace);
    tenant.getNamespaces().addAll(Namespace.toNamespace(namespaces, tenant));

    return tenant;
  }

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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name="tenant_id")
  private Set<Namespace> namespaces = new HashSet<Namespace>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tenant")
  private Set<TenantUser> users = new HashSet<TenantUser>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "tenant_generator", joinColumns = @JoinColumn(name = "tenant_id"),
      inverseJoinColumns = @JoinColumn(name = "generator_id"))
  private List<Generator> generators = new ArrayList<>();
  
  @ManyToOne
  @JoinColumn(name="owner_id")
  private User owner;

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

  public void addGenerator(Generator generator) {
    generators.add(generator);
    generator.getTenants().add(this);
  }

  public void removeGenerator(Generator generator) {
    generators.remove(generator);
    generator.getTenants().remove(this);
  }

  public Set<User> getTenantAdmins() {
    return users.stream().filter(user -> user.hasRole(Role.TENANT_ADMIN))
        .map(user -> user.getUser()).collect(Collectors.toSet());
  }
  
  public boolean hasUser(String username) {
    return users.stream().anyMatch(user -> user.getUser().getUsername().equals(username));
  }
  
  public Set<TenantUser> getTenantUserAdmins() {
    return users.stream().filter(user -> user.hasRole(Role.TENANT_ADMIN))
        .collect(Collectors.toSet());
  }
  
  public Optional<TenantUser> getUser(String userId) {
    return users.stream().filter(user -> user.getUser().getUsername().equals(userId)).findAny();
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
  
  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
    owner.addOwnedTenant(this);
  }

  public List<Generator> getGenerators() {
    return generators;
  }

  public void setGenerators(List<Generator> generators) {
    this.generators = generators;
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

  @Override
  public String toString() {
    return "Tenant [id=" + id + ", tenantId=" + tenantId + ", defaultNamespace=" + defaultNamespace
        + ", authenticationProvider=" + authenticationProvider + ", authorizationProvider="
        + authorizationProvider + ", namespaces=" + namespaces + ", users=" + users
        + ", generators=" + generators + "]";
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
