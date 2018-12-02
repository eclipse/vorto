package org.eclipse.vorto.repository.core.security;

import javax.jcr.Credentials;

import org.springframework.security.core.Authentication;

public class SpringSecurityCredentials implements Credentials {

	private static final long serialVersionUID = 1L;

	private transient Authentication authentication;

	public SpringSecurityCredentials(Authentication authentication) {
		this.authentication = authentication;
	}

	public Authentication getAuthentication() {
		return authentication;
	}
}
