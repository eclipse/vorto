/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web.security;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class UserAuthoritiesExtractor implements AuthoritiesExtractor {

	@Autowired
	public IUserAccountService userService;
	
	@Override
	public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
		String username = (String) map.get("login");
		 User user = userService.getUser(username);
		 if (user == null) {
			 return Collections.<GrantedAuthority> emptyList();
		 }
		return AuthorityUtils.createAuthorityList(user.getUserRolesAsCommaSeparatedString());
	}

}
