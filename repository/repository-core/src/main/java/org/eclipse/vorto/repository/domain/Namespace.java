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
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "namespace")
public class Namespace {

  public static Collection<Namespace> toNamespace(Collection<String> namespaces, Tenant tenant) {
    return namespaces.stream().map(name -> {
      Namespace namespace = new Namespace();
      namespace.setName(name);
      namespace.setTenant(tenant);
      return namespace;
    }).collect(Collectors.toList());
  }
  
  public static Namespace newNamespace(String name) {
    Namespace namespace = new Namespace();
    namespace.setName(name);
    return namespace;
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tenant_id")
  private Tenant tenant;
  
  @NaturalId
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }
}
