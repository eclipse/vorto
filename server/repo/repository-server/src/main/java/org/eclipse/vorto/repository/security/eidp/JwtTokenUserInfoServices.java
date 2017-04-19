package org.eclipse.vorto.repository.security.eidp;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JwtTokenUserInfoServices extends UserInfoTokenServices {

	private Interceptor interceptor;
	private OAuth2RestOperations restTemplate;
	private String clientId;
	private Gson gson = new Gson();
	
	public JwtTokenUserInfoServices(String userInfoEndpointUrl, String clientId) {
		super(userInfoEndpointUrl, clientId);
		this.clientId = clientId;
	}
	
	public JwtTokenUserInfoServices(String userInfoEndpointUrl, String clientId, Interceptor interceptor) {
		this(userInfoEndpointUrl, clientId);
		this.interceptor = interceptor;
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)	
			throws AuthenticationException, InvalidTokenException {
		OAuth2Authentication auth = authenticationFromToken(getMap(accessToken));
		if (interceptor != null) {
			interceptor.intercept(restTemplate, auth, accessToken);
		}
		
		return auth;
	}
	
	private OAuth2Authentication authenticationFromToken(Map<String, String> map) {
		OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
				null, null, null, null);
		
		String nameFromEmail = map.get("email").split("@")[0];
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				nameFromEmail, "N/A", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		
		Map<String, String> detailsMap = new HashMap<String, String>();
		detailsMap.put("sub", map.get("sub"));
		detailsMap.put("name", nameFromEmail);
		detailsMap.put("given_name", map.get("email"));
		detailsMap.put("last_name", map.get("email"));
		detailsMap.put("email", map.get("email"));
		authToken.setDetails(detailsMap);
		
		return new OAuth2Authentication(request, authToken);
	}
	
	private Map<String, String> getMap(String accessToken) {
		String[] jwtParts = accessToken.split("\\.");
		
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		return gson.fromJson(new String(Base64.getUrlDecoder().decode(jwtParts[1])), type);
	}

	@Override
	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
		super.setRestTemplate(restTemplate);
	}

	public Interceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
}
