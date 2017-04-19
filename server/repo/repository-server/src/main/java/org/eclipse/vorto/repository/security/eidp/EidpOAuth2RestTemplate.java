package org.eclipse.vorto.repository.security.eidp;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class EidpOAuth2RestTemplate extends OAuth2RestTemplate {

	private static final String RESOURCE2 = "resource";

	public EidpOAuth2RestTemplate(EidpResourceDetails resource, OAuth2ClientContext context) {
		super(resource, context);
	}

	@Override
	protected OAuth2AccessToken acquireAccessToken(OAuth2ClientContext oauth2Context)
			throws UserRedirectRequiredException {
		try {
			return super.acquireAccessToken(oauth2Context);
		} catch(UserRedirectRequiredException e) {
			e.getRequestParams().put(RESOURCE2, ((EidpResourceDetails) getResource()).getResource());
			throw e;
		}
	}
}
