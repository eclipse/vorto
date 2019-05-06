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
package org.eclipse.vorto.repository.web.security;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;

public class UserDBAuthoritiesExtractor implements AuthoritiesExtractor {

  @Autowired
  public IUserAccountService userService;

  private String userAttributeId = null;

  public UserDBAuthoritiesExtractor(String userAttributeId) {
    this.userAttributeId = userAttributeId;
  }

  @Override
  public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
    String username = (String) map.get(userAttributeId);
    User user = userService.getUser(username);
    if (user == null) {
      return Collections.<GrantedAuthority>emptyList();
    }
    
    return SpringUserUtils.toAuthorityList(user.getAllRoles());
  }

}
