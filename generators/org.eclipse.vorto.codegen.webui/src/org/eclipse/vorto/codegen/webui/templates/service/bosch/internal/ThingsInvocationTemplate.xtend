package org.eclipse.vorto.codegen.webui.templates.service.bosch.internal

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingsInvocationTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingsInvocationTemplate.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/internal'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.internal;
		
		import org.apache.http.HttpHeaders;
		import org.apache.http.client.methods.HttpUriRequest;
		import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.core.context.SecurityContextHolder;
		import org.springframework.security.oauth2.client.OAuth2ClientContext;
		import org.springframework.security.oauth2.provider.OAuth2Authentication;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.permissions.IMUserInfo;
		import com.google.gson.GsonBuilder;
		
		public class ThingsInvocationTemplate extends AsyncInvocationTemplate {
		
			private String apiToken;
			private OAuth2ClientContext oauthClientContext;
		
			private final static String CR_API_TOKEN_HEADER = "x-cr-api-token";
			
			public ThingsInvocationTemplate(String apiToken, OAuth2ClientContext oauthClientContext) {
				super(new GsonBuilder().create());
				this.apiToken = apiToken;
				this.oauthClientContext = oauthClientContext;
			}
		
			@Override
			protected void preSend(HttpUriRequest request) {
				super.preSend(request);
				request.addHeader(CR_API_TOKEN_HEADER, apiToken);
				request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());
			}
		
			private String getToken() {
				Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
				if (autentication instanceof OAuth2Authentication) {
					return (String) oauthClientContext.getAccessToken().getAdditionalInformation().get("id_token");
				} else if (autentication instanceof UsernamePasswordAuthenticationToken) {
					IMUserInfo userInfo =  (IMUserInfo)((UsernamePasswordAuthenticationToken)autentication).getDetails();
					return userInfo.getAuthorizationToken().getJwt();
				} else {
					throw new RuntimeException("User is not yet authenticated.");
				}
			}
			
			
		
		}
		
		'''
	}
	
}