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
package org.eclipse.vorto.server.devtool.web.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.server.devtool.models.GlobalContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/context")
public class ContextController {

	@Autowired
	private GlobalContext globalContext;
	
	@RequestMapping(method = RequestMethod.GET)
	public GlobalContext getGlobalContext() {
		return globalContext;
	}

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getUser(Principal user, final HttpServletRequest request) {

		Map<String, String> map = new LinkedHashMap<>();

		if (user == null)
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.UNAUTHORIZED);

		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			map.put("role", authority.getAuthority());
		}

		map.put("name", user.getName());

		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
}
