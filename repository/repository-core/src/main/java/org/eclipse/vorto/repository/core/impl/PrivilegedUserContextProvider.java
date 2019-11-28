/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 * <p>
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author kolotu
 */
public class PrivilegedUserContextProvider {

  public static IUserContext systemAdminContext() {
    return UserContext.user(createAdminTechnicalUser("admin"));
  }

  public static IUserContext systemAdminContext(String originalUserName) {
    return UserContext.user(createAdminTechnicalUser(originalUserName));
  }

  private static Authentication createAdminTechnicalUser(String username) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_SYS_ADMIN));
    return new UsernamePasswordAuthenticationToken(username, username, authorities);
  }

}
