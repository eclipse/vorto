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
package org.eclipse.vorto.repository.core.impl;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author kolotu This class contains a generic mechanism to create a user context with elevated
 * privileges (SysAdmin privileges).
 */
public class PrivilegedUserContextProvider {

  /**
   * Create a SysAdmin user context with the default username "admin".
   *
   * @return a user context with SysAdmin privileges.
   */
  public static IUserContext systemAdminContext() {
    return UserContext.user(createAdminTechnicalUser("admin"));
  }

  /**
   * Create a SysAdmin user context with the given username.
   *
   * @return a user context with SysAdmin privileges.
   */
  public static IUserContext systemAdminContext(String originalUserName) {
    return UserContext.user(createAdminTechnicalUser(originalUserName));
  }

  private static Authentication createAdminTechnicalUser(String username) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(RepositoryRole.SYS_ADMIN.getName()));
    return new UsernamePasswordAuthenticationToken(username, username, authorities);
  }

}
