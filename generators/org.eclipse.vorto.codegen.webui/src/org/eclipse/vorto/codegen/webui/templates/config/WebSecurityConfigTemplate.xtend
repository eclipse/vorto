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
package org.eclipse.vorto.codegen.webui.templates.config

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class WebSecurityConfigTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''WebSecurityConfig.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/config'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».config;
		
		import java.util.Arrays;
		import java.util.Optional;
		
		import javax.servlet.Filter;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
		import org.springframework.boot.context.properties.ConfigurationProperties;
		import org.springframework.boot.web.servlet.FilterRegistrationBean;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.core.annotation.Order;
		import org.springframework.security.config.annotation.web.builders.HttpSecurity;
		import org.springframework.security.config.annotation.web.builders.WebSecurity;
		import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
		import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
		import org.springframework.security.oauth2.client.OAuth2ClientContext;
		import org.springframework.security.oauth2.client.OAuth2RestTemplate;
		import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
		import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
		import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
		import org.springframework.security.oauth2.client.token.AccessTokenProvider;
		import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
		import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
		import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
		import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
		import org.springframework.web.filter.CompositeFilter;
		
		@Configuration
		@EnableWebSecurity
		@EnableOAuth2Client
		@Order(6)
		public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
			
			@Autowired
			private OAuth2ClientContext oauth2ClientContext;
				
			@Autowired
			private AuthorizationCodeResourceDetails google;
			
			@Value("${google.oauth2.resource.userInfoUri}")
			private String googleUserInfoUri;
					
			@Override
			public void configure(WebSecurity web) throws Exception {
				web.ignoring().antMatchers("/webjars/**","/css/**","/js/**","/dist/**");
			}
				
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests().antMatchers("/rest/identities/user/**","/rest/devices/**").authenticated().and().logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index.html");
				http.addFilterAt(ssoFilter(), BasicAuthenticationFilter.class);
				http.formLogin().loginPage("/login");
				http.csrf().disable();
			
			}
			
			private Filter ssoFilter() {
				CompositeFilter filter = new CompositeFilter();
				filter.setFilters(Arrays.asList(googleFilter()));
				return filter;
			}
			
			private Filter googleFilter() {
				UserInfoTokenServices tokenService = new UserInfoTokenServices(googleUserInfoUri, google.getClientId());
				return newSsoFilter("/google/login", google, tokenService, Optional.empty(), 
				new OAuth2RestTemplate(google, oauth2ClientContext));		
			}
				
			private Filter newSsoFilter(String defaultFilterProcessesUrl, OAuth2ProtectedResourceDetails resource, 
					UserInfoTokenServices tokenService, Optional<AccessTokenProvider> accessTokenProvider,
					OAuth2RestTemplate restTemplate) {
				accessTokenProvider.ifPresent(provider -> restTemplate.setAccessTokenProvider(provider));
			
				OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(defaultFilterProcessesUrl);
				
				tokenService.setRestTemplate(restTemplate);
				filter.setRestTemplate(restTemplate);
				filter.setTokenServices(tokenService);
				
				return filter;
			}
			
			@Bean
			public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
				FilterRegistrationBean registration = new FilterRegistrationBean();
				registration.setFilter(filter);
				registration.setOrder(-100);
				return registration;
			}
				
			@Bean
			@ConfigurationProperties("google.oauth2.client")
			public AuthorizationCodeResourceDetails google() {
				return new AuthorizationCodeResourceDetails();
			}
		}
		'''
	}
	
}