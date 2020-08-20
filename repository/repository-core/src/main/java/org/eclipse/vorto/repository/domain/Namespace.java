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

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.utils.NamespaceUtils;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "namespace", indexes = {
    @Index(name = "idx_namespace_name", columnList = "name", unique = true),
    @Index(name = "idx_namespace_workspace_id", columnList = "workspace_id", unique = true)
})
public class Namespace {

  public static final String PRIVATE_NAMESPACE_PREFIX = "vorto.private.";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "workspace_id")
  private String workspaceId;

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

  public String getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(String workspaceId) {
    this.workspaceId = workspaceId;
  }

  public boolean owns(ModelId modelId) {
    return NamespaceUtils.in(getName(), NamespaceUtils.components(modelId.getNamespace()));
  }

  public boolean owns(String namespace) {
    return NamespaceUtils.in(getName(), NamespaceUtils.components(namespace));
  }

  public boolean isInConflictWith(String namespace) {
    return NamespaceUtils.in(namespace, NamespaceUtils.components(getName())) || NamespaceUtils.in(getName(), NamespaceUtils.components(namespace));
  }

}
