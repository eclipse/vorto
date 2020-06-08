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

import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.IRole;
import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import java.util.Objects;

public class SpringSecurityContext implements SecurityContext {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityContext.class);

  private SpringSecurityCredentials credentials;

  public SpringSecurityContext(SpringSecurityCredentials credentials) {
    this.credentials = credentials;
  }

  @Override
  public boolean isAnonymous() {
    return credentials.getAuthentication() instanceof AnonymousAuthenticationToken;
  }

  @Override
  public String getUserName() {
    return credentials.getAuthentication().getName();
  }

  @Override
  public boolean hasRole(String principalName) {
    if (Objects.nonNull(principalName) && principalName.startsWith("ROLE_")) {
      principalName = principalName.replace("ROLE_", "");
    }
    // grant named model owners access to their models
    if (principalName.equals(credentials.getAuthentication().getName())
        || principalName.equals(UserContext.getHash(credentials.getAuthentication().getName()))) {
      return true;
    }

    // grant sys ads access to the models
    if (credentials.isSysAdmin()) {
      return true;
    }

    // grant people with certain roles access to the models
    if (credentials.hasPrivilege(principalName)) {
      return true;
    }
    if (credentials.getRolesInNamespace().stream().map(IRole::getName).anyMatch(principalName::equalsIgnoreCase)) {
      return true;
    }

    // grant non-members of the tenant (including anonymous users) access to models
    if (IModelPolicyManager.ANONYMOUS_ACCESS_POLICY.equals(principalName)) {
      return true;
    }

    // grant everyone read access to all repositories
    return "readonly".equals(principalName);
  }

  @Override
  public void logout() {
    LOGGER.debug("logout of Vorto Repository");
  }

}
