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
package org.eclipse.vorto.repository.web.admin;

import java.util.Collection;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;

@RestController
@RequestMapping(value = "/rest/{tenantId}/diagnostics")
public class DiagnosticsController {

  @Autowired
  private IModelRepositoryFactory repoFactory;

  @Autowired
  private ITenantService tenantService;
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  public Collection<Diagnostic> diagnose() {
    Collection<Diagnostic> diagnostics = Lists.newArrayList();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    for (Tenant tenant : tenantService.getTenants()) {
      Collection<Diagnostic> tenantDiagnostics =
          repoFactory.getDiagnosticsService(tenant.getTenantId(), auth).diagnoseAllModels();
      tenantDiagnostics.forEach(diagnostic -> diagnostic.setTenantId(tenant.getTenantId()));
      diagnostics.addAll(tenantDiagnostics);
    }

    return diagnostics;
  }

}
