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
package org.eclipse.vorto.repository.web.config;

import java.util.Arrays;

import javax.servlet.Filter;

import org.eclipse.vorto.repository.sso.AuthorizationTokenFilter;
import org.eclipse.vorto.repository.sso.InterceptedUserInfoTokenServices;
import org.eclipse.vorto.repository.sso.boschid.EidpOAuth2RestTemplate;
import org.eclipse.vorto.repository.sso.boschid.EidpResourceDetails;
import org.eclipse.vorto.repository.sso.boschid.JwtTokenUserInfoServices;
import org.eclipse.vorto.repository.web.AngularCsrfHeaderFilter;
import org.eclipse.vorto.repository.web.TenantVerificationFilter;
import org.eclipse.vorto.repository.web.listeners.AuthenticationEntryPoint;
import org.eclipse.vorto.repository.web.listeners.AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;
	
	@Autowired
	private EidpResourceDetails eidp;
	
	@Autowired
	private AuthorizationCodeResourceDetails github;
	
	@Autowired
	private AccessTokenProvider accessTokenProvider;
	
	@Autowired
	private InterceptedUserInfoTokenServices interceptedUserInfoTokenServices;
	
	@Autowired
	private AuthoritiesExtractor authoritiesExtractor;

	@Autowired
	private TenantVerificationFilter tenantVerificationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic()
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/rest/**","/api/**").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/rest/**","/api/**").permitAll()
				.antMatchers(HttpMethod.POST, "/rest/**","/api/**").authenticated()
				.antMatchers(HttpMethod.DELETE, "/rest/**","/api/**").authenticated()
			.and()
				.addFilterAfter(new AngularCsrfHeaderFilter(), CsrfFilter.class)
				.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(bearerTokenFilter(), SecurityContextPersistenceFilter.class)
				.addFilterAfter(tenantVerificationFilter, SecurityContextPersistenceFilter.class)
				.csrf()
					.csrfTokenRepository(csrfTokenRepository())
			.and()
				.csrf()
					.disable()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
			.and()
				.headers()
					.frameOptions()
					.sameOrigin()
					.httpStrictTransportSecurity()
					.disable();
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
	}
	
	@Bean
	public static PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(
	    OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	}
	
	private Filter bearerTokenFilter() {
		return new AuthorizationTokenFilter(interceptedUserInfoTokenServices);
	}
	
	private Filter ssoFilter() {
		CompositeFilter filter = new CompositeFilter();
		filter.setFilters(Arrays.asList(githubFilter(), eidpFilter()));
		return filter;
	}
	
	private Filter githubFilter() {
		return newSsoFilter("/github/login", interceptedUserInfoTokenServices, accessTokenProvider, 
				new OAuth2RestTemplate(github, oauth2ClientContext));		
	}
	
	private Filter eidpFilter() {
		UserInfoTokenServices tokenService = new JwtTokenUserInfoServices("https://accounts.bosch.com/adfs/userinfo", eidp.getClientId());
		return newSsoFilter("/eidp/login", tokenService, accessTokenProvider, 
				new EidpOAuth2RestTemplate(eidp, oauth2ClientContext));
	}
	
	private Filter newSsoFilter(String defaultFilterProcessesUrl, UserInfoTokenServices tokenService, AccessTokenProvider accessTokenProvider,
			OAuth2RestTemplate restTemplate) {
		restTemplate.setAccessTokenProvider(accessTokenProvider);
		
		OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(defaultFilterProcessesUrl);
		filter.setAuthenticationSuccessHandler(successHandler);
		tokenService.setRestTemplate(restTemplate);
		tokenService.setAuthoritiesExtractor(authoritiesExtractor);
		filter.setRestTemplate(restTemplate);
		filter.setTokenServices(tokenService);
		
		return filter;
	}
	
	@Bean
	@ConfigurationProperties("eidp.oauth2.client")
	public EidpResourceDetails eidp() {
		return new EidpResourceDetails();
	}
	
	@Bean
	@ConfigurationProperties("github.oauth2.client")
	public AuthorizationCodeResourceDetails github() {
		return new AuthorizationCodeResourceDetails();
	}
}