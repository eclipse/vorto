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
package org.eclipse.vorto.repository.web.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller shares the same base path as {@link org.eclipse.vorto.repository.web.api.v1.NamespaceController},
 * but it lives in its own package scoped to administrator functionalities. <br/>
 * It only provides one endpoint to diagnose all namespaces (formerly "tenants"). <br/>
 * This is only available to sysadmin users, so all namespaces are diagnosed without filter. <br/>
 */
@RestController
@RequestMapping(value = "/rest/namespaces/diagnostics")
public class DiagnosticsController {

  @Autowired
  private IModelRepositoryFactory repoFactory;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @GetMapping
  @PreAuthorize("hasAuthority('sysadmin')")
  public Collection<Diagnostic> diagnose() {
    Collection<Diagnostic> diagnostics = new ArrayList<>();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    diagnostics = namespaceRepository.findAll().stream().flatMap(
        namespace -> repoFactory
            .getDiagnosticsService(namespace.getWorkspaceId(), auth)
            .diagnoseAllModels()
            .stream()
            .map(d -> d.setWorkspaceId(namespace.getWorkspaceId()))
    )
        .collect(Collectors.toList());

    return diagnostics;
  }

}
