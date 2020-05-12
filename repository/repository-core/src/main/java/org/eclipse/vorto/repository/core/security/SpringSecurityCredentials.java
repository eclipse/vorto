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
package org.eclipse.vorto.repository.core.security;

import org.eclipse.vorto.repository.domain.IRole;
import org.springframework.security.core.Authentication;

import javax.jcr.Credentials;
import java.util.Set;

public class SpringSecurityCredentials implements Credentials {

  private static final long serialVersionUID = -671171242483089098L;

  private Authentication authentication;

  private Set<IRole> rolesInTenant;

  public SpringSecurityCredentials(Authentication authentication, Set<IRole> rolesInTenant) {
    this.authentication = authentication;
    this.rolesInTenant = rolesInTenant;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public Set<IRole> getRolesInTenant() {
    return rolesInTenant;
  }
}
