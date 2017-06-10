/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.web.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Returns the currently logged in User")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized"), 
							@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getUser(Principal user, final HttpServletRequest request) {
		
		Map<String, String> map = new LinkedHashMap<>();
		
		if(user == null)
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
				
		OAuth2Authentication oauth2User = (OAuth2Authentication) user;
		map.put("name", oauth2User.getName());
		map.put("email", ((Map<String, String>) oauth2User.getUserAuthentication().getDetails()).get("email"));
		
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}

}
