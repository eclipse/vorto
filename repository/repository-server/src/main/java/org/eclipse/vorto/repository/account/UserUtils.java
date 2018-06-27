package org.eclipse.vorto.repository.account;

import org.eclipse.vorto.repository.account.impl.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class UserUtils {
	public static void refreshSpringSecurityUser(User user) {
		// We only need to replace the authorities as that might be the only thing that changed
		OAuth2Authentication oauth2Auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

		UsernamePasswordAuthenticationToken oldAuth = (UsernamePasswordAuthenticationToken) oauth2Auth.getUserAuthentication();

		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(oldAuth.getPrincipal(),
				oldAuth.getCredentials(), AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toString()));
		newAuth.setDetails(oldAuth.getDetails());
		
		OAuth2Authentication newOAuthAuth = new OAuth2Authentication(oauth2Auth.getOAuth2Request(), newAuth);
		newOAuthAuth.setDetails(oauth2Auth.getDetails());
		
		SecurityContextHolder.getContext().setAuthentication(newOAuthAuth);
	}
}
