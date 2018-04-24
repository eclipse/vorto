package org.eclipse.vorto.repository.web.account;

import java.security.Principal;

import org.eclipse.vorto.repository.web.security.VortoUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class UserUtils {
	public static boolean sameUser(Principal user1, String user2) {
		assert(user1 != null);
		assert(user2 != null);
		
		String user1Name = null;
		if (user1 instanceof OAuth2Authentication) {
			user1Name = ((OAuth2Authentication) user1).getName();
		} else {
			user1Name = ((VortoUser) ((UsernamePasswordAuthenticationToken) user1).getPrincipal()).getUsername();
		}
		
		return user1Name.equals(user2);
	}
	
	public static boolean isAdmin(Principal user) {
		return sameUser(user, "admin");
	}
}
