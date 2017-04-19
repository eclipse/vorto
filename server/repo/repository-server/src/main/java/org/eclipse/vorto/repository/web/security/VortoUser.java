package org.eclipse.vorto.repository.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class VortoUser extends User {

	private static final long serialVersionUID = -5732782740718902153L;
	
	private String email;
	
	private org.eclipse.vorto.repository.model.User user;

	public VortoUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	public VortoUser(org.eclipse.vorto.repository.model.User user) {
		this(user.getUsername(), user.getPassword(), true, true, true, true,
        		  AuthorityUtils.createAuthorityList("ROLE_"+user.getRole().toString()));
		this.email = user.getEmail();
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public org.eclipse.vorto.repository.model.User getUser() {
		return user;
	}

	public void setUser(org.eclipse.vorto.repository.model.User user) {
		this.user = user;
	}
}
