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
package org.eclipse.vorto.repository.server.config.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class MyAnonymousAuthFilter extends GenericFilterBean {

	@Value("${server.config.authenticatedSearchMode:#{null}}") 
	private boolean authenticatedAccess;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if (!authenticatedAccess && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			AnonymousAuthenticationToken anonymousToken = (AnonymousAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
			AnonymousAuthenticationToken newAnonymousToken = new AnonymousAuthenticationToken(Integer.toString(anonymousToken.getKeyHash()),anonymousToken.getPrincipal(),AuthorityUtils.createAuthorityList("ROLE_USER"));
			SecurityContextHolder.getContext().setAuthentication(newAnonymousToken);
		}
		
		chain.doFilter(request, response);
	}

}
