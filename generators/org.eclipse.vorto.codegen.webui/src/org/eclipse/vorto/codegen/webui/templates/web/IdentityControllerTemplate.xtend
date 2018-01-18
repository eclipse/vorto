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
package org.eclipse.vorto.codegen.webui.templates.web

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class IdentityControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''IdentityController.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.xdk.web;
		
		import java.lang.reflect.Type;
		import java.util.Base64;
		import java.util.HashMap;
		import java.util.Map;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.http.MediaType;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.oauth2.client.OAuth2ClientContext;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.RestController;
		
		import com.google.gson.Gson;
		import com.google.gson.reflect.TypeToken;
		
		@RestController
		@RequestMapping("/rest/identities")
		public class IdentityController {
			
			@Autowired
			private OAuth2ClientContext oauthClientContext;
		
			@RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			public Map<String, String> user(Authentication user) {
				Map<String, String> principal = new HashMap<String, String>();
				principal.put("userId", user.getName());
				principal.put("userSub", getUserSub(user));
				return principal;
			}
		
			private String getUserSub(Authentication user) {
				String token = (String) oauthClientContext.getAccessToken().getAdditionalInformation().get("id_token");
				return getJwtTokenMap(token).get("sub");
			}
			
			public Map<String, String> getJwtTokenMap(String accessToken) {
				String[] jwtParts = accessToken.split("\\.");
				Type type = new TypeToken<Map<String, String>>(){}.getType();
				return new Gson().fromJson(new String(Base64.getUrlDecoder().decode(jwtParts[1])), type);
			}
		}
		'''
	}
	
}
