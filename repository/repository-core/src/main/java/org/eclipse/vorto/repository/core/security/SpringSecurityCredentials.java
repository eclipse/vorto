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
package org.eclipse.vorto.repository.core.security;

import java.util.Set;
import javax.jcr.Credentials;
import org.eclipse.vorto.repository.domain.Role;
import org.springframework.security.core.Authentication;

public class SpringSecurityCredentials implements Credentials {

  private static final long serialVersionUID = -671171242483089098L;

  private Authentication authentication;

  private Set<Role> rolesInTenant;

  public SpringSecurityCredentials(Authentication authentication, Set<Role> rolesInTenant) {
    this.authentication = authentication;
    this.rolesInTenant = rolesInTenant;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public Set<Role> getRolesInTenant() {
    return rolesInTenant;
  }
}
