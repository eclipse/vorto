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

import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Credentials;
import java.util.Map;
import java.util.Objects;

public class SpringSecurityProvider implements AuthenticationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityProvider.class);

	private String name;

	@Override
	public ExecutionContext authenticate(Credentials credentials, String repositoryName, String workspaceName,
		ExecutionContext repositoryContext, Map<String, Object> sessionAttributes) {

		if (credentials instanceof SpringSecurityCredentials) {
			SpringSecurityCredentials springCredentials = (SpringSecurityCredentials) credentials;
			if (Objects.nonNull(springCredentials.getAuthentication())) {
				LOGGER.debug("[{}] Successfully authenticated.", springCredentials.getAuthentication().getName());
				return repositoryContext.with(new SpringSecurityContext(springCredentials));
			}
		}
		return null;
	}
}
