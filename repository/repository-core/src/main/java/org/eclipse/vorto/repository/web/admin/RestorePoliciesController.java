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

import io.swagger.annotations.ApiParam;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restore all access policies on all accessible models.
 * Mitigates: (https://github.com/eclipse/vorto/issues/2350)
 */

@RestController
public class RestorePoliciesController {

  @Autowired
  private IModelRepositoryFactory repoFactory;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @PostMapping("/rest/models/restorepolicies")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ResponseEntity<String> restorePolicies() {
    namespaceRepository.findAll().forEach(
        n -> this.repoFactory
            .getPolicyManager(n.getWorkspaceId())
            .restorePolicyEntries()
    );
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  @PostMapping("/rest/models/{namespace}/restorepolicies")
  @PreAuthorize("hasAuthority('sysadmin')")
  public ResponseEntity<String> restorePoliciesForNamespace(
      @ApiParam(value = "The namespace for which the policies should be restored",
          required = true) final @PathVariable String namespace) {
    String workspaceId = namespaceRepository.findByName(namespace).getWorkspaceId();
    this.repoFactory.getPolicyManager(workspaceId).restorePolicyEntries();
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

}
