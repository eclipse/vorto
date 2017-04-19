package org.eclipse.vorto.repository.security.eidp;

import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface Interceptor {
	public void intercept(OAuth2RestOperations restTemplate, OAuth2Authentication authentication, String accessToken);
}
