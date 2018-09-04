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
package org.eclipse.vorto.repository.sso.oauth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.sso.oauth.strategy.CiamUserStrategy;
import org.eclipse.vorto.repository.sso.oauth.strategy.KeycloakUserStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("jwtTokenUserInfoServices")
public class JwtTokenUserInfoServices extends UserInfoTokenServices {

	private static final String ISSUER = "iss";

	@Value("${eidp.oauth2.client.clientId: #{null}}")
	private String ciamClientId;
	
	@Value("${oauth2.verification.eidp.issuer: #{null}}")
	private String ciamJwtIssuer;
	
	@Value("${oauth2.verification.eidp.publicKeyUri: #{null}}")
	private String ciamPublicKeyUri;
	
	@Value("${oauth2.verification.keycloak.issuer: #{null}}")
	private String keycloakJwtIssuer;
	
	@Value("${oauth2.verification.keycloak.publicKeyUri: #{null}}")
	private String keycloakPublicKeyUri;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	private Map<String, JwtVerifyAndIdStrategy> verifyAndIdStrategies = new HashMap<>();
	
	public JwtTokenUserInfoServices() {
		super(null, null);
	}
	
	private Optional<JwtVerifyAndIdStrategy> getStrategy(JwtToken jwtToken) {
		String issuer = (String) jwtToken.getPayloadMap().get(ISSUER);
		if (issuer != null) {
			return Optional.ofNullable(verifyAndIdStrategies.get(issuer));
		}
		
		return Optional.empty();
	}

	@PostConstruct
	public void init() {
		if (ciamJwtIssuer != null) {
			verifyAndIdStrategies.put(ciamJwtIssuer, new CiamUserStrategy(new RestTemplate(), ciamPublicKeyUri, userAccountService, ciamClientId));
		}
		
		if (keycloakJwtIssuer != null) {
			verifyAndIdStrategies.put(keycloakJwtIssuer, new KeycloakUserStrategy(new RestTemplate(), keycloakPublicKeyUri, userAccountService, ciamClientId));
		}
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		
		JwtToken jwtToken = JwtToken.instance(accessToken).orElseThrow(
				() -> new InvalidTokenException("The access token '" + accessToken + "' is not a proper JWT token."));
		
		Optional<JwtVerifyAndIdStrategy> strategyToUse = getStrategy(jwtToken);
		if(strategyToUse.isPresent()) {
			JwtVerifyAndIdStrategy strategy = strategyToUse.get();
		 	if (strategy.verify(jwtToken)) {
		 		return strategy.createAuthentication(jwtToken);
		 	}
		 	
		 	throw new InvalidTokenException("The JWT token '" + accessToken + "' cannot be verified. Either it is malformed, the user isn't registered, or it has already expired.");
		}
		
		throw new InvalidTokenException("No strategy for authenticating the JWT token. Tokens must have 'iss' whose issuers are configured in Thingtype.");
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}
}
