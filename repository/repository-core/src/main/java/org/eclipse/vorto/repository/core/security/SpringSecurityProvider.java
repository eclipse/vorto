package org.eclipse.vorto.repository.core.security;

import java.util.Map;

import javax.jcr.Credentials;

import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class SpringSecurityProvider implements AuthenticationProvider {

	final static Logger logger = LoggerFactory.getLogger(SpringSecurityProvider.class);

	@Override
	public ExecutionContext authenticate(Credentials credentials, String repositoryName, String workspaceName,
			ExecutionContext repositoryContext, Map<String, Object> sessionAttributes) {

		if (credentials instanceof SpringSecurityCredentials) {
			SpringSecurityCredentials creds = (SpringSecurityCredentials) credentials;
			Authentication auth = creds.getAuthentication();
			if (auth != null) {
				logger.debug("[{}] Successfully authenticated.", auth.getName());
				return repositoryContext.with(new SpringSecurityContext(auth));
			}
		}
		return null;
	}
}
