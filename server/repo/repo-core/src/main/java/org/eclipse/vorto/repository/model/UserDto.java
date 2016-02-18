/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.repository.model;

import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDto implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4036429074853939005L;

    private String firstName;

    private String lastName;

    @NotNull
    @Size(min = 5)
    private String username;
    
    @NotNull
    private String email;
    
    @NotNull
    private boolean hasWatchOnRepository;
    
    @NotNull
    @Size(min = 6) 
    private String password;

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public boolean getHasWatchOnRepository() {
        return hasWatchOnRepository;
    }

    public void setHasWatchOnRepository(boolean hasWatchOnRepository) {
        this.hasWatchOnRepository = hasWatchOnRepository;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [firstName=").append(firstName).append("]").append("[lastName=").append(lastName).append("]").append("[username=").append(username).append("]").append("[email=").append(email).append("]").append("[password=").append(password).append("[role=").append(role).append("]");
        return builder.toString();
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}