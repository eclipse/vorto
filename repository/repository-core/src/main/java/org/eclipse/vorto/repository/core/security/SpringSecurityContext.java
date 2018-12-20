package org.eclipse.vorto.repository.core.security;

import java.util.Set;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

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
	    if (roleName.equals(authentication.getName()) || roleName.equals(UserContext.user(authentication.getName()).getHashedUsername())) {
	      return true;
	    }
	    
	    Set<Role> userRoles = SpringUserUtils.authorityListToSet(authentication.getAuthorities());
	   
	    for (Role userRole : userRoles) {
	      if (userRole.hasPermission(roleName) || (Role.isValid(roleName) && Role.of(roleName) == userRole)) {
	        return true;
	      }
	    }
	    
	    return false;
	    
//		for (GrantedAuthority authority : authentication.getAuthorities()) {
//			if (roleName.equals("readonly") && (authority.getAuthority().equalsIgnoreCase("role_user") || authority.getAuthority().equalsIgnoreCase("role_admin"))) {
//				return true;
//			} else if (roleName.equals("readwrite") && (authority.getAuthority().equalsIgnoreCase("role_model_creator") || authority.getAuthority().equalsIgnoreCase("role_admin"))) {
//				return true;
//			} else if (roleName.equals("ROLE_ADMIN") && authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
//				return true;
//			}
//		}
//
//		return false;
	}

	@Override
	public void logout() {
		logger.debug("logout of Vorto Repository");
	}

}
