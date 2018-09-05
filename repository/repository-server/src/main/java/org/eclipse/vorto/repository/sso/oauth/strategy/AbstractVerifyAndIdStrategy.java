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

import java.security.PublicKey;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.eclipse.vorto.repository.sso.oauth.JwtVerifyAndIdStrategy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractVerifyAndIdStrategy implements JwtVerifyAndIdStrategy {

	protected static final String JWT_EMAIL = "email";
	private static final String JWT_EXPIRY = "exp";
	protected static final String JWT_NAME = "name";
	protected static final String JWT_SUB = "sub";
	private static final String KEY_ID = "kid";
	
	protected static final String ROLE_ADMIN = "ROLE_ADMIN";
	protected static final String ROLE_USER = "ROLE_USER";
	
	private String ciamClientId;
	private PublicKeyHelper publicKeyHelper;
	private Map<String, PublicKey> publicKeys = null;
	private String publicKeyUri = null;
	private IUserAccountService userAccountService;
	
	public AbstractVerifyAndIdStrategy(RestTemplate restTemplate, String publicKeyUri, IUserAccountService userAccountService, String clientId) {
		this.publicKeyHelper = PublicKeyHelper.instance(restTemplate);
		this.publicKeyUri = Objects.requireNonNull(publicKeyUri);
		this.ciamClientId = Objects.requireNonNull(clientId);
		this.userAccountService = Objects.requireNonNull(userAccountService);
	}
	
	@Override
	public OAuth2Authentication createAuthentication(JwtToken accessToken) {
		OAuth2Request request = new OAuth2Request(null, this.ciamClientId, null, true, null, null, null, null, null);

		Map<String, Object> map = accessToken.getPayloadMap();
		
		Optional<String> email = Optional.ofNullable((String) map.get(JWT_EMAIL));
		Optional<String> name = Optional.ofNullable((String) map.get(JWT_NAME)).map(str -> str.split("@")[0]);
		
		String userId = getUserId(map).orElseThrow(() -> 
			new InvalidTokenException("Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));
		User user = userAccountService.getUser(userId);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(name.orElse(userId), "N/A",
				AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole() == Role.USER ? ROLE_USER: ROLE_ADMIN));

		Map<String, String> detailsMap = new HashMap<String, String>();
		detailsMap.put(JWT_SUB, userId);
		detailsMap.put(JWT_NAME, name.orElse(userId));
		detailsMap.put(JWT_EMAIL, email.orElse(null));
		authToken.setDetails(detailsMap);

		return new OAuth2Authentication(request, authToken);
	}

	protected abstract Optional<String> getUserId(Map<String, Object> map);
	
	@Override
	public boolean verify(JwtToken jwtToken) {
		if (publicKeys == null || publicKeys.isEmpty()) {
			publicKeys = publicKeyHelper.getPublicKey(publicKeyUri);
		}
		
		String keyId = (String) jwtToken.getHeaderMap().get(KEY_ID); 
		if (keyId == null) {
			throw new InvalidTokenException(String.format("AccessToken '%s' doesn't have a kid in header", jwtToken.getJwtToken()));
		}
		
		PublicKey publicKey = publicKeys.get(keyId);
		if (publicKey == null) {
			throw new InvalidTokenException(String.format("There are no public keys with kid '%s'", keyId));
		}
		
		if (VerificationHelper.verifyJwtToken(publicKey, jwtToken)) {
			Map<String, Object> payloadMap = jwtToken.getPayloadMap();
			if (payloadMap.containsKey(JWT_EXPIRY)) {
				Optional<Instant> expirationDate = Optional.ofNullable(Double.valueOf((double) payloadMap.get(JWT_EXPIRY)).longValue()).map(Instant::ofEpochSecond);
				if (expirationDate.isPresent() && expirationDate.get().isBefore(Instant.now())) {
					return false;
				}
			}
			
			String userId = getUserId(payloadMap).orElseThrow(() -> 
				new InvalidTokenException("Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));
			
			return userAccountService.exists(userId);
		}
		
		return false;
	}

}
