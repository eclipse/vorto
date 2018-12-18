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
package org.eclipse.vorto.repository.utils;

import java.util.Map;
import javax.jcr.Credentials;
import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;

public class DummySecurityProvider implements AuthenticationProvider {

	@Override
	public ExecutionContext authenticate(Credentials credentials, String repositoryName, String workspaceName,
			ExecutionContext repositoryContext, Map<String, Object> sessionAttributes) {
	  
		if (credentials != null && credentials instanceof DummySecurityCredentials) {
			DummySecurityCredentials creds = (DummySecurityCredentials) credentials;
			return repositoryContext.with(new DummySecurityContext(creds));
		}
		return null;
	}
}
