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
package org.eclipse.vorto.repository.security;

import java.util.Map;

import org.eclipse.vorto.repository.service.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class InterceptedUserInfoTokenServices extends UserInfoTokenServices {

	@Autowired
    private IUserRepository userRepository;
	
	@Autowired
	public InterceptedUserInfoTokenServices(
			@Value("${github.oauth2.resource.userInfoUri}") String userInfoEndpointUrl, 
			@Value("${github.oauth2.client.clientId}") String clientId) {
		super(userInfoEndpointUrl, clientId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		OAuth2Authentication auth = super.loadAuthentication(accessToken);
		
		if (auth != null) {
			Map<String, Object> userDetails = ((Map<String, Object>) auth.getUserAuthentication().getDetails());
			
			if (userRepository.findByEmail((String) userDetails.get("email")) == null) {
				userDetails.put("isRegistered", "false");
			} else {
				userDetails.put("isRegistered", "true");
			}
			
			userDetails.put(SecurityConfiguration.LOGIN_TYPE, "github");
		}
		
		return auth;
	}
}
