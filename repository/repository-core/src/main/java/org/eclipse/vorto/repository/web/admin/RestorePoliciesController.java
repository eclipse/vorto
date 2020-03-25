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
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Restore all access policies on all accessible models.
 * Mitigates: (https://github.com/eclipse/vorto/issues/2350)
 */

@RestController
public class RestorePoliciesController {

    @Autowired
    private ITenantService tenantService;

    @Autowired
    private IModelRepositoryFactory repoFactory;

    @RequestMapping(value = "/rest/models/restorepolicies", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
    public ResponseEntity<String> restorePolicies() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (Tenant tenant : tenantService.getTenants()) {
            this.repoFactory.getPolicyManager(tenant.getTenantId(), auth).restorePolicyEntries();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/models/{namespace}/restorepolicies")
    @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
    public ResponseEntity<String> restorePoliciesForNamespace(
            @ApiParam(value = "The namespace for which the policies should be restored",
                    required = true) final @PathVariable String namespace,
            @RequestParam("file") MultipartFile file) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.repoFactory.getPolicyManager(namespace, auth).restorePolicyEntries();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
