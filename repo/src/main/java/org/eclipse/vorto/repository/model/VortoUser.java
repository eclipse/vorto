/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class VortoUser implements UserDetails{
	

	private Long id;

	private String name;

	private String httpAuthHeader;
	
	private Set<String> roles = new HashSet<String>();
	
	private UserDetails seed;

	public VortoUser(UserDetails userDetails){
		this.seed = userDetails;
	}

	public VortoUser(String name)
	{
		this.name = name;
	}
	

	public Long getId()
	{
		return this.id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public String getName()
	{
		if(this.name == null && seed!=null){
			this.name = seed.getUsername();
		}
		return this.name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public Set<String> getRoles()
	{
		if(this.roles==null || this.roles.isEmpty()){			
			if(seed!=null){
				this.roles = new HashSet<String>();
				for(GrantedAuthority authority : seed.getAuthorities()){
					roles.add(authority.getAuthority());
				}
			}
		}
		return this.roles;
	}


	public void setRoles(Set<String> roles)
	{
		this.roles = roles;
	}


	public void addRole(String role)
	{
		this.roles.add(role);
	}

	

	public String getHttpAuthHeader() {
		return httpAuthHeader;
	}


	public void setHttpAuthHeader(String httpAuthHeader) {
		this.httpAuthHeader = httpAuthHeader;
	}


	@Override
	public String getPassword()
	{
		return seed.getPassword();
	}


	public void setPassword(String password)
	{
		
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if(getRoles() == null && seed!=null){
			return seed.getAuthorities();
		}
		
		Set<String> roles = this.getRoles();

		if (roles == null) {
			return Collections.emptyList();
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}


	@Override
	public String getUsername()
	{
		return this.name;
	}


	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}


	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}


	@Override
	public boolean isEnabled()
	{
		return true;
	}
}
