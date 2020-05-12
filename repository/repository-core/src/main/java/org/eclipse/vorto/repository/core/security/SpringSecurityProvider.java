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
import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import javax.jcr.Credentials;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SpringSecurityProvider implements AuthenticationProvider {

	private final static Logger logger = LoggerFactory.getLogger(SpringSecurityProvider.class);

	@Override
	public ExecutionContext authenticate(Credentials credentials, String repositoryName, String workspaceName,
			ExecutionContext repositoryContext, Map<String, Object> sessionAttributes) {

		if (credentials instanceof SpringSecurityCredentials) {
			SpringSecurityCredentials creds = (SpringSecurityCredentials) credentials;
			Authentication auth = creds.getAuthentication();
			Set<IRole> rolesInTenant = creds.getRolesInTenant();//TODO 2265
			if (auth != null) {
				logger.debug("[{}] Successfully authenticated.", auth.getName());
				return repositoryContext.with(new SpringSecurityContext(auth, Collections.emptySet()));
			}
		}
		return null;
	}
}
