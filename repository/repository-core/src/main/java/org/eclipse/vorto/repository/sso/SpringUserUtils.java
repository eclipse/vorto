package org.eclipse.vorto.repository.sso;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.account.UserUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class SpringUserUtils {

	public static void refreshSpringSecurityUser(User user) {
		// We only need to replace the authorities as that might be the only thing that changed
		OAuth2Authentication oldAuthentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

		UsernamePasswordAuthenticationToken oldAuth = (UsernamePasswordAuthenticationToken) oldAuthentication.getUserAuthentication();

		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(oldAuth.getPrincipal(),
				oldAuth.getCredentials(), AuthorityUtils.commaSeparatedStringToAuthorityList(UserUtils.getUserRolesAsCommaSeparatedString(user)));
		newAuth.setDetails(oldAuth.getDetails());

		OAuth2Authentication newAuthentication = new OAuth2Authentication(oldAuthentication.getOAuth2Request(), newAuth);
		newAuthentication.setDetails(oldAuthentication.getDetails());
		
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
	
	public static List<GrantedAuthority> toAuthorityList(Set<Role> roles) {
		Set<String> roleStrings = roles.stream().map(role -> "ROLE_"+ role.toString()).collect(Collectors.toSet());
	    return AuthorityUtils.createAuthorityList(roleStrings.toArray(new String[roleStrings.size()]));
	}
}
