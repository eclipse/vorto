/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.repository.web;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class HomeController {
	
	private static final String LOGIN_TYPE = "loginType";

	@Value("${github.oauth2.enabled}")
	private boolean githubEnabled;
	
	@Value("${eidp.oauth2.enabled}")
	private boolean eidpEnabled;
	
	@Autowired
	private IUserAccountService accountService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Returns the currently logged in User")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized"), 
							@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(value ={ "/user", "/me" }, method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getUser(Principal user, final HttpServletRequest request) {
		
		Map<String, String> map = new LinkedHashMap<>();
		
		if(user == null)
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
		
		OAuth2Authentication oauth2User = (OAuth2Authentication) user;
		
		oauth2User.getAuthorities().stream().findFirst().ifPresent(role -> map.put("role", role.getAuthority()));
		
		map.put("name", oauth2User.getName());
		map.put("isRegistered", Boolean.toString(accountService.exists(oauth2User.getName())));
		Map<String, String> userDetails = ((Map<String, String>) oauth2User.getUserAuthentication().getDetails());
		map.put("loginType", userDetails.get(LOGIN_TYPE));
		
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value ={ "/context" }, method = RequestMethod.GET)
	public Map<String, Object> globalContext() {
		Map<String, Object> context = new LinkedHashMap<>();
		
		context.put("githubEnabled", githubEnabled);
		context.put("eidpEnabled", eidpEnabled);
		
		return context;
	}
	
}
