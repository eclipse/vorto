package org.eclipse.vorto.repository.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class VortoUser extends User {

	private static final long serialVersionUID = -5732782740718902153L;
	private org.eclipse.vorto.repository.account.impl.User user;

	public VortoUser(String username, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, null, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	public VortoUser(org.eclipse.vorto.repository.account.impl.User user) {
		this(user.getUsername(), true, true, true, true,
        		  AuthorityUtils.createAuthorityList("ROLE_"+ user.getRole().toString()));
		this.user = user;
	}

	public org.eclipse.vorto.repository.account.impl.User getUser() {
		return user;
	}

	public void setUser(org.eclipse.vorto.repository.account.impl.User user) {
		this.user = user;
	}
}
