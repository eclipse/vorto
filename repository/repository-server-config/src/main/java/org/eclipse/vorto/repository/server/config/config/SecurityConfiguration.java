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
package org.eclipse.vorto.repository.server.config.config;

import java.util.Arrays;

import javax.servlet.Filter;

import org.eclipse.vorto.repository.sso.AuthorizationTokenFilter;
import org.eclipse.vorto.repository.sso.InterceptedUserInfoTokenServices;
import org.eclipse.vorto.repository.sso.boschid.EidpOAuth2RestTemplate;
import org.eclipse.vorto.repository.sso.boschid.EidpPrincipalExtractor;
import org.eclipse.vorto.repository.sso.boschid.EidpResourceDetails;
import org.eclipse.vorto.repository.sso.oauth.SimpleUserInfoServices;
import org.eclipse.vorto.repository.web.listeners.AuthenticationEntryPoint;
import org.eclipse.vorto.repository.web.listeners.AuthenticationSuccessHandler;
import org.eclipse.vorto.repository.web.security.UserDBAuthoritiesExtractor;
import org.eclipse.vorto.repository.web.tenant.TenantVerificationFilter;
import org.eclipse.vorto.repository.web.ui.AngularCsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
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
	@Qualifier("githubUserInfoTokenService")
	private InterceptedUserInfoTokenServices interceptedUserInfoTokenServices;
	
	@Autowired
	@Qualifier("simpleUserInfoServices")
	private SimpleUserInfoServices simpleUserInfoService;
	
	@Autowired
	private EidpResourceDetails eidp;
		
	
	@Value("${github.oauth2.resource.userInfoUri}") 
	private String githubUserInfoEndpointUrl;
	
	@Value("${eidp.oauth2.resource.userInfoUri}") 
	private String eidpUserInfoEndpointUrl;
	
	@Value("${github.oauth2.enabled}")
	private boolean githubEnabled = false; 
	
	@Value("${server.config.generatorUser}")
	private String generatorUser = ""; 
	
	@Value("${server.config.generatorPassword}")
	private String generatorUserPassword = ""; 
	
	@Autowired
	private AuthorizationCodeResourceDetails github;
	
	@Autowired
	private AccessTokenProvider accessTokenProvider;
	
	@Autowired
	private TenantVerificationFilter tenantVerificationFilter;
	
	private static final String ROLE_GENERATOR_PROVIDER = "GENERATOR_PROVIDER";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic()
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/rest/**","/api/**").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/rest/**","/api/**").authenticated()
				.antMatchers(HttpMethod.POST, "/rest/**","/api/**").authenticated()
				.antMatchers(HttpMethod.DELETE, "/rest/**","/api/**").authenticated()
			.and()
				.addFilterAfter(new AngularCsrfHeaderFilter(), CsrfFilter.class)
				.addFilterAfter(anonymousFilter(),AnonymousAuthenticationFilter.class)
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
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(generatorUser).password(generatorUserPassword).roles(ROLE_GENERATOR_PROVIDER);
	}

	
	@Bean
	public Filter anonymousFilter() {
		return new MyAnonymousAuthFilter();
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
		if (githubEnabled) {
			return new AuthorizationTokenFilter(interceptedUserInfoTokenServices);
		} else {
			return new AuthorizationTokenFilter(simpleUserInfoService);
		}
	}
	
	private Filter ssoFilter() {
		CompositeFilter filter = new CompositeFilter();
		filter.setFilters(Arrays.asList(githubFilter(), eidpFilter()));
		return filter;
	}
	
	private Filter githubFilter() {
		return newSsoFilter("/github/login", interceptedUserInfoTokenServices, accessTokenProvider, 
				new OAuth2RestTemplate(github, oauth2ClientContext),authoritiesExtractor("login"));		
	}
	
	private Filter eidpFilter() {
		SimpleUserInfoServices userInfoService = new SimpleUserInfoServices(eidpUserInfoEndpointUrl, eidp);
		userInfoService.setPrincipalExtractor(new EidpPrincipalExtractor());
		return newSsoFilter("/eidp/login", userInfoService, accessTokenProvider, 
				new EidpOAuth2RestTemplate(eidp, oauth2ClientContext),authoritiesExtractor("sub"));
	}
	
	private Filter newSsoFilter(String defaultFilterProcessesUrl, UserInfoTokenServices tokenService, AccessTokenProvider accessTokenProvider,
			OAuth2RestTemplate restTemplate, AuthoritiesExtractor authoritiesExtractor ) {
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
	@Scope("prototype")
	public AuthoritiesExtractor authoritiesExtractor(String userAttributeId) {
		return new UserDBAuthoritiesExtractor(userAttributeId);
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