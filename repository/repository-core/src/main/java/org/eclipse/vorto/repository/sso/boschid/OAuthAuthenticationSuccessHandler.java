package org.eclipse.vorto.repository.sso.boschid;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuthAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	private OAuth2ClientContext oauthClientContext;
	
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if (authentication instanceof OAuth2Authentication && !oauthClientContext.getAccessToken().getAdditionalInformation().isEmpty()) {
			String jwtToken = extractToken(authentication);
			setAuthenticationDetails(authentication, getJwtTokenMap(jwtToken));
		}
				
		super.handle(request, response, authentication);
	}
	
	private void setAuthenticationDetails(Authentication authentication, Map<String, String> tokenMap) {
		UsernamePasswordAuthenticationToken userAuthentication = 
				(UsernamePasswordAuthenticationToken)((OAuth2Authentication) authentication).getUserAuthentication();
		
		Map<String, String> detailsMap = new HashMap<String, String>();
		detailsMap.put("sub", tokenMap.get("sub"));
		detailsMap.put("name", tokenMap.get("email"));
		detailsMap.put("given_name", tokenMap.get("email"));
		detailsMap.put("last_name", tokenMap.get("email"));
		detailsMap.put("email", tokenMap.get("email"));
		
		userAuthentication.setDetails(detailsMap);
	}

	private String extractToken(Authentication authentication) {
		if (authentication instanceof OAuth2Authentication) {
			return (String) oauthClientContext.getAccessToken().getAdditionalInformation().get("id_token");
		} else {
			throw new RuntimeException("User is not authenticated yet");
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getJwtTokenMap(String accessToken) {
		String[] jwtParts = accessToken.split("\\.");
		JSONObject claims = new JSONObject(new String(Base64.getUrlDecoder().decode(jwtParts[1])));
		
		Map<String, String> map = new HashMap<String, String>();
		claims.keySet().stream().forEach(key -> map.put((String) key, claims.get((String) key).toString()));
		
		return map;
	}

}
