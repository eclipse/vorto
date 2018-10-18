/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.sso.oauth.strategy;

import java.util.Map;
import java.util.Optional;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.springframework.web.client.RestTemplate;

public class CiamUserStrategy extends AbstractVerifyAndIdStrategy {
	
	protected static final String JWT_CLIENT_ID = "client_id";
	
	public CiamUserStrategy(RestTemplate restTemplate, String publicKeyUri, IUserAccountService userAccountService,
			String clientId, String resourceClientId) {
		super(restTemplate, publicKeyUri, userAccountService, clientId, resourceClientId);
	}

	protected Optional<String> getUserId(Map<String, Object> map) {
		Optional<String> userId = Optional.ofNullable((String) map.get(JWT_SUB));
		if (!userId.isPresent()) {
			return Optional.ofNullable((String) map.get(JWT_CLIENT_ID));
		}
		
		return userId;
	}
}
