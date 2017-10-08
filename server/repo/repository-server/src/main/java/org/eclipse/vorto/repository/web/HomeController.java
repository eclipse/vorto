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

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.config.SecurityConfiguration;
import org.eclipse.vorto.repository.web.security.VortoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="Home Controller", description="REST API to get currently logged in User ")
@RestController
public class HomeController {

	@Value("${github.oauth2.enabled}")
	private boolean githubEnabled;
	
	@Value("${eidp.oauth2.enabled}")
	private boolean eidpEnabled;
	
	@Value("${webEditor.enabled}")
	private boolean webEditorEnabled;
	
	@Value("${webEditor.loginUrl.github}")
	private String githubLoginUrl;
	
	@Value("${webEditor.loginUrl.eidp}")
	private String eidpLoginUrl;
	
	@Value("${webEditor.loginUrl.default}")
	private String defaultLoginUrl;
	
	@Autowired
    private IUserRepository userRepository;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Returns the currently logged in User")
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized"), 
							@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(value ={ "/user", "/me" }, method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getUser(Principal user, final HttpServletRequest request) {
		
		Map<String, String> map = new LinkedHashMap<>();
		
		if(user == null)
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);
		
		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
			map.put("role", authority.getAuthority());
		}
		
		if (user instanceof OAuth2Authentication) {
			OAuth2Authentication oauth2User = (OAuth2Authentication) user;
			String email = ((Map<String, String>) oauth2User.getUserAuthentication().getDetails()).get("email");
			map.put("name", oauth2User.getName());
			map.put("email",  email);
			
			Map<String, String> userDetails = ((Map<String, String>) oauth2User.getUserAuthentication().getDetails()); 
			//map.put("isRegistered", userDetails.get("isRegistered"));
			map.put("isRegistered", Boolean.toString(userRepository.findByEmail(email) != null));
			map.put("loginType", userDetails.get(SecurityConfiguration.LOGIN_TYPE));
		} else {
			VortoUser vortoUser = (VortoUser) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
			map.put("name", vortoUser.getUsername());
			map.put("email", vortoUser.getEmail());
			map.put("isRegistered", "true");
			map.put("loginType", "default");
		}
		
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value ={ "/context" }, method = RequestMethod.GET)
	public Map<String, Object> globalContext() {
		Map<String, Object> context = new LinkedHashMap<>();
		
		context.put("githubEnabled", githubEnabled);
		context.put("eidpEnabled", eidpEnabled);
		context.put("webEditor", getWebEditorContext());
		
		return context;
	}
	
	public Map<String, Object> getWebEditorContext() {
		Map<String, Object> webEditorContext = new LinkedHashMap<>();
		
		Map<String, Object> loginContext = new LinkedHashMap<>();
		loginContext.put("default", defaultLoginUrl);
		loginContext.put("github", githubLoginUrl);
		loginContext.put("eidp", eidpLoginUrl);
		
		webEditorContext.put("enabled", webEditorEnabled);
		webEditorContext.put("loginUrl", loginContext);
		
		return webEditorContext;
	}
}
