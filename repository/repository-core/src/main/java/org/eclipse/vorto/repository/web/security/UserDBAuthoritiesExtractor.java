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
package org.eclipse.vorto.repository.web.security;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDBAuthoritiesExtractor implements AuthoritiesExtractor {

  @Autowired
  public IUserAccountService userService;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private RoleService roleService;

  private String userAttributeId = null;

  public UserDBAuthoritiesExtractor(String userAttributeId) {
    this.userAttributeId = userAttributeId;
  }

  @Override
  @Transactional
  public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
    String username = (String) map.get(userAttributeId);
    User user = userService.getUser(username);
    if (user == null) {
      return Collections.emptyList();
    }
    Set<IRole> allRoles = getAllRoles(user);
    if (allRoles.isEmpty()) {
      allRoles.add(roleService.findAnyByName("user").orElseThrow(() -> new IllegalStateException("Role 'user' is not present.")));
    }

    return SpringUserUtils.toAuthorityList(allRoles);
  }

  private Set<IRole> getAllRoles(User user) {
    return userNamespaceRoleService.getRolesOnAllNamespaces(user);
  }

}
