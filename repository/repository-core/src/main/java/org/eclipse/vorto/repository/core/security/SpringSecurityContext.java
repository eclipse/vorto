package org.eclipse.vorto.repository.core.security;

import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SpringSecurityContext implements SecurityContext {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityContext.class);

	private Authentication authentication;

	public SpringSecurityContext(Authentication authentication) {
		this.authentication = authentication;
	}

	@Override
	public boolean isAnonymous() {
		return authentication instanceof AnonymousAuthenticationToken;
	}

	@Override
	public String getUserName() {
		return authentication.getName();
	}

	@Override
	public boolean hasRole(String roleName) {
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (roleName.equals("readonly") && (authority.getAuthority().equalsIgnoreCase("role_user") || authority.getAuthority().equalsIgnoreCase("role_admin"))) {
				return true;
			} else if (roleName.equals("readwrite") && (authority.getAuthority().equalsIgnoreCase("role_model_creator") || authority.getAuthority().equalsIgnoreCase("role_admin"))) {
				return true;
			} else if (roleName.equals("admin") && authority.getAuthority().equalsIgnoreCase("role_admin")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void logout() {
		logger.info("logout");
	}

}
