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

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

public class JwtTokenUserInfoServices extends UserInfoTokenServices {

	private Interceptor interceptor;
//	private OAuth2RestOperations restTemplate;
//	private String clientId;
//	private Gson gson = new Gson();
	
	public JwtTokenUserInfoServices(String userInfoEndpointUrl, String clientId) {
		super(userInfoEndpointUrl, clientId);
//		this.clientId = clientId;
	}
	
	public JwtTokenUserInfoServices(String userInfoEndpointUrl, String clientId, Interceptor interceptor) {
		this(userInfoEndpointUrl, clientId);
		this.interceptor = interceptor;
	}

//	@Override
//	public OAuth2Authentication loadAuthentication(String accessToken)	
//			throws AuthenticationException, InvalidTokenException {
//		OAuth2Authentication auth = authenticationFromToken(getMap(accessToken));
//		if (interceptor != null) {
//			interceptor.intercept(restTemplate, auth, accessToken);
//		}
//		
//		return auth;
//	}
	
//	private OAuth2Authentication authenticationFromToken(Map<String, String> map) {
//		OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
//				null, null, null, null);
//		
//		String nameFromEmail = map.get("email").split("@")[0];
//		
//		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//				nameFromEmail, "N/A", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
//		
//		Map<String, String> detailsMap = new HashMap<String, String>();
//		detailsMap.put("sub", map.get("sub"));
//		detailsMap.put("name", nameFromEmail);
//		detailsMap.put("given_name", map.get("email"));
//		detailsMap.put("last_name", map.get("email"));
//		detailsMap.put("email", map.get("email"));
//		authToken.setDetails(detailsMap);
//		
//		return new OAuth2Authentication(request, authToken);
//	}
	
//	private Map<String, String> getMap(String accessToken) {
//		String[] jwtParts = accessToken.split("\\.");
//		
//		Type type = new TypeToken<Map<String, String>>(){}.getType();
//		return gson.fromJson(new String(Base64.getUrlDecoder().decode(jwtParts[1])), type);
//	}

//	@Override
//	public void setRestTemplate(OAuth2RestOperations restTemplate) {
//		this.restTemplate = restTemplate;
//		super.setRestTemplate(restTemplate);
//	}

	public Interceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
}
