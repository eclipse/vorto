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
package org.eclipse.vorto.repository.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.eclipse.vorto.repository.model.VortoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;


@Component
@Path("/user")
public class UserResource
{

	@Autowired
	private UserDetailsService userService;
//
	@Autowired
//	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;


	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */
	@Path("verify")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public VortoUser getUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			throw new WebApplicationException(401);
		}
		UserDetails seed = (UserDetails) principal;
		VortoUser userDetails = new VortoUser(seed);
		String httpAuthHeader = "Basic " + new String(Base64.encode((seed.getUsername()+":"+seed.getPassword()).getBytes()));
		userDetails.setHttpAuthHeader(httpAuthHeader);
		return userDetails;
	}


	/**
	 * Authenticates a user and creates an authentication token.
	 * 
	 * @param username
	 *            The name of the user.
	 * @param password
	 *            The password of the user.
	 * @return A transfer containing the authentication token.
	 */
	@Path("authenticate")
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public VortoUser authenticate(@FormParam("username") String username, @FormParam("password")String password)
	{

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
		 * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
		UserDetails seed = this.userService.loadUserByUsername(username);
		VortoUser userDetails = new VortoUser(seed);
		String httpAuthHeader = "Basic " + new String(Base64.encode((username+":"+password).getBytes()));
		userDetails.setHttpAuthHeader(httpAuthHeader);
		return userDetails;
	}

//
//	private Map<String, Boolean> createRoleMap(UserDetails userDetails)
//	{
//		Map<String, Boolean> roles = new HashMap<String, Boolean>();
//		for (GrantedAuthority authority : userDetails.getAuthorities()) {
//			roles.put(authority.getAuthority(), Boolean.TRUE);
//		}
//
//		return roles;
//	}

}