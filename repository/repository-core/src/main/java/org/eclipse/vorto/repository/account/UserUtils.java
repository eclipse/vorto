package org.eclipse.vorto.repository.account;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.account.impl.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class UserUtils {
	public static void refreshSpringSecurityUser(User user) {
		// We only need to replace the authorities as that might be the only thing that changed
		OAuth2Authentication oldAuthentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

		UsernamePasswordAuthenticationToken oldAuth = (UsernamePasswordAuthenticationToken) oldAuthentication.getUserAuthentication();

		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(oldAuth.getPrincipal(),
				oldAuth.getCredentials(), AuthorityUtils.commaSeparatedStringToAuthorityList(getUserRolesAsCommaSeparatedString(user)));
		newAuth.setDetails(oldAuth.getDetails());

		OAuth2Authentication newAuthentication = new OAuth2Authentication(oldAuthentication.getOAuth2Request(), newAuth);
		newAuthentication.setDetails(oldAuthentication.getDetails());
		
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}

	public static String getUserRolesAsCommaSeparatedString(User user) {
		Set<Role> userRoles = UserUtils.extractRolesAsList(user.getRoles());
		StringJoiner roles = new StringJoiner(",");

		for (Role userRole : userRoles) {
			roles.add("ROLE_" + userRole);

		}
		return roles.toString();
	}
	 
	public static Set<Role> extractRolesAsList(Set<UserRole> userRoles) {
		Set<Role> existingRole = new HashSet<>();

		if(userRoles == null)
			return existingRole;

		for(UserRole userRole : userRoles) {
			existingRole.add(userRole.getRole());
		}
		return existingRole;
	}

	public static List<GrantedAuthority> toAuthorityList(Set<Role> roles) {
		Set<String> roleStrings = roles.stream().map(role -> "ROLE_"+ role.toString()).collect(Collectors.toSet());
	    return AuthorityUtils.createAuthorityList(roleStrings.toArray(new String[roleStrings.size()]));
	}
	
}
