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
package org.eclipse.vorto.server.devtool.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.web.OrderedHttpPutFormContentFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@EnableOAuth2Client
@Order(6)
public abstract class AbstractSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	protected OAuth2ClientContext oauth2ClientContext;
	
	@Bean
	public OrderedHttpPutFormContentFilter httpPutFormContentFilter() {
		return new OrderedHttpPutFormContentFilter() {
			@Override
			protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				filterChain.doFilter(request, response);
			}
		};
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().and().authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and()
				.addFilterAfter(new AngularCsrfHeaderFilter(), CsrfFilter.class)
				.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class).csrf()
				.csrfTokenRepository(csrfTokenRepository()).and().csrf().disable().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/").and().headers().frameOptions().sameOrigin().httpStrictTransportSecurity()
				.disable();
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	@Bean
	@ConfigurationProperties("github.client")
	public AuthorizationCodeResourceDetails github() {
		return new AuthorizationCodeResourceDetails();
	}
	
	@Bean
	@ConfigurationProperties("github.resource")
	public ResourceServerProperties githubResource() {
		return new ResourceServerProperties();
	}

	protected abstract Filter ssoFilter();
}
