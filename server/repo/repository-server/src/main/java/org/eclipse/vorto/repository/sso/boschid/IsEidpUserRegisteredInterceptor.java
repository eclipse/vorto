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
package org.eclipse.vorto.repository.sso.boschid;

import java.util.Map;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.config.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class IsEidpUserRegisteredInterceptor implements Interceptor {

	@Autowired
    private IUserRepository userRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public void intercept(OAuth2RestOperations restTemplate, OAuth2Authentication authentication, String accessToken) {
		Map<String, String> userDetails = ((Map<String, String>) authentication.getUserAuthentication().getDetails());
		
		if (userRepository.findByEmail(userDetails.get("email")) == null) {
			userDetails.put("isRegistered", "false");
		} else {
			userDetails.put("isRegistered", "true");
		}
		
		userDetails.put(SecurityConfiguration.LOGIN_TYPE, "eidp");
	}

}
