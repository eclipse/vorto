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
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

public class SpringSecurityContext implements SecurityContext {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityContext.class);

	private Authentication authentication;

	public SpringSecurityContext(Authentication authentication) {
		this.authentication = authentication;
	}

	@Override
	public boolean isAnonymous() {
		return authentication instanceof AnonymousAuthenticationToken;
	}

	@Override
	public String getUserName() {
		return authentication.getName();
	}

	@Override
	public boolean hasRole(String roleName) {
	    if (roleName.equals(authentication.getName()) || roleName.equals(UserContext.user(authentication.getName()).getHashedUsername())) {
	      return true;
	    }
	    
	    Set<Role> userRoles = SpringUserUtils.authorityListToSet(authentication.getAuthorities());
	   
	    for (Role userRole : userRoles) {
	      if (userRole.hasPermission(roleName) || (Role.isValid(roleName) && Role.of(roleName) == userRole)) {
	        return true;
	      }
	    }
	    
	    return false;
	}

	@Override
	public void logout() {
		logger.debug("logout of Vorto Repository");
	}

}
